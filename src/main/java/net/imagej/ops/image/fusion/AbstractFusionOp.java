package net.imagej.ops.image.fusion;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.OpService;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

public abstract class AbstractFusionOp<T> extends AbstractFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> {

	@Parameter(type=ItemIO.INPUT)
	protected RandomAccessibleInterval<T> in2;
	
	@Parameter(type=ItemIO.INPUT)
	protected long[] offset;
	
	@Parameter
	protected OpService ops;
	
	
	protected FinalInterval calculateOutputSize(RandomAccessibleInterval<T> input1, RandomAccessibleInterval<T> input2, long[] offset) {
		long[] outImgsize = new long[input1.numDimensions()];
        for (int i = 0; i < input1.numDimensions(); i++) {
            outImgsize[i] = input1.dimension(i) + input2.dimension(i)
                    - (input1.dimension(i) - Math.abs(offset[i]));
        }

        FinalInterval outInterval =
                Intervals.createMinMax(0, 0, outImgsize[0], outImgsize[1]);
        return outInterval;
	}
	
	protected long[] updatePosition(long[] currentPosition, long[] offset) {
		long[] newPosition = new long[currentPosition.length];
		newPosition[0] = currentPosition[0];
		newPosition[1] = currentPosition[1];

        if (offset[0] > -1) {
        	newPosition[0] -= offset[0];
        }
        if (offset[1] > -1) {
        	newPosition[1] -= offset[1];
        }
        return newPosition;
	}
}
