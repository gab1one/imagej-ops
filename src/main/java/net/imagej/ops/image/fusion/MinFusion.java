package net.imagej.ops.image.fusion;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Image.FuseMin;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

@Plugin(type = Ops.Image.FuseMin.class, name = Ops.Image.FuseMin.NAME)
public class MinFusion<T extends RealType<T>>
		extends AbstractFusionOp<T> implements FuseMin {
	
	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> in1) {
		
        FinalInterval outInterval = calculateOutputSize(in1, in2, offset);
                
		long[] maxPos = new long[in2.numDimensions()];
        in2.max(maxPos);
        in2.randomAccess().setPosition(maxPos);
        RandomAccess<T> img1 =
                Views.extendValue(in1, in2.randomAccess().get()).randomAccess();

        in1.max(maxPos);
        in1.randomAccess().setPosition(maxPos);

        // moving in1 such that in1 and in2 have the same point in their
        // origin
        RandomAccess<T> img2 =
                Views.offset(Views.extendValue(in2, in1.randomAccess().get()),
                        offset).randomAccess();

        @SuppressWarnings("unchecked")
		T type = (T) ops.create().nativeType(img1.get().getClass());
        Img<T> outImg = ops.create().img(outInterval, type);
        Cursor<T> outCursor = outImg.localizingCursor();
        long[] pos = new long[outImg.numDimensions()];
        long[] img1Pos = new long[outImg.numDimensions()];
        long[] img2Pos = new long[outImg.numDimensions()];
        while (outCursor.hasNext()) {
            outCursor.fwd();
            outCursor.localize(pos);

            // moving cursor positions according to the offset, otherwise we
            // miss the real origin in certain situations
            img1Pos = updatePosition(pos, offset);
            img2Pos = updatePosition(pos, offset);

            img1.setPosition(img1Pos);
            img2.setPosition(img2Pos);

            T img1Value = img1.get();
            T img2Value = img2.get();

            if (img1Value.compareTo(img2Value) < 0) {
                outCursor.get().set(img1Value);
            } else {
                outCursor.get().set(img2Value);
            }
        }
        return outImg;
	}
}
