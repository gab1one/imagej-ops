package net.imagej.ops.filter.phasecorrelation;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.PhaseCorrelate;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

@Plugin(type=Ops.Filter.PhaseCorrelate.class, name=Ops.Filter.PhaseCorrelate.NAME)
public class ImgPhaseCorrelationOp<T extends RealType<T>> extends AbstractFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements PhaseCorrelate {

	@Parameter(type=ItemIO.INPUT)
	private RandomAccessibleInterval<T> interval1;
	
	@Parameter(type=ItemIO.INPUT)
	private float normalizationThreshold;
	
	@Parameter(type=ItemIO.INPUT, required=false)
	private int test;
	
	@Parameter
	private OpService ops;
	
	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> input) {
				
		// TODO compute FFT, (correlate, multiply => normalizeAndConjugate),
		// calculate inverse FFT
		ops.filter().fft(input);
		ops.filter().fft(interval1);
		
		// call normalizeComplexImgOp 
		ops.image().normalize(input, normalizationThreshold);
		ops.image().normalizeConjugate((Img<T>) interval1, normalizationThreshold);
				
		
		// maybe create new output instance instead of using interval1 as output
		ops.filter().ifft(interval1, input);
		
		return interval1; // return result
	}
	
	
	

}
