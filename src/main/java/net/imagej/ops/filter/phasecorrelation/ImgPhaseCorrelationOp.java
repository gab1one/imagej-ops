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
import net.imglib2.view.Views;

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
				
		// calculate inverse FFT
		RandomAccessibleInterval<T> fft1 = (RandomAccessibleInterval<T>) ops.filter().fft(input);
		RandomAccessibleInterval<T> fft2 = (RandomAccessibleInterval<T>) ops.filter().fft(interval1);
		
		// call normalizeComplexImgOp 
		RandomAccessibleInterval<T> img1 = ops.image().normalize(fft1, normalizationThreshold);
		RandomAccessibleInterval<T> img2 = ops.image().normalizeConjugate(fft2, normalizationThreshold);
		
		Cursor<T> fft1cursor = Views.flatIterable(fft1).cursor();
		Cursor<T> fft2cursor = Views.flatIterable(fft2).cursor();
		
		
		while (fft1cursor.hasNext()) {
            fft1cursor.next().mul(fft2cursor.next());
        }
		
		// maybe create new output instance instead of using img2 as output
		ops.filter().ifft(img2, img1);
		
		return img2; // return result
	}
	
	
	

}
