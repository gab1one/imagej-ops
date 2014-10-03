
package net.imagej.ops.fft.methods;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Fft;
import net.imagej.ops.fft.image.AbstractFftImage;
import net.imglib2.FinalDimensions;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

/**
 * Forward FFT op implemented by wrapping FFTMethodsJTransform.
 * 
 * @author bnorthan
 *
 * @param <T>
 * @param <I>
 */
@Plugin(type = AbstractFftImage.class, name = Fft.NAME)
public class FftMethodsJTransformWrapperOp<T extends RealType<T>, I extends Img<T>> extends
	AbstractFftImage<T, I, ComplexFloatType, Img<ComplexFloatType>>
{
	
	@Override
	protected long[] getFftFastSize(long[] inputSize) {
		FinalDimensions dim=new FinalDimensions(inputSize);
		
		long[] paddedSize=new long[inputSize.length];
		long[] fftSize=new long[inputSize.length];
		
		FFTMethodsJTransform.dimensionsRealToComplexFast(dim, paddedSize, fftSize);
		
		return fftSize;
	}
	
	@Override
	protected long[] getFftSmallSize(long[] inputSize) {
	FinalDimensions dim=new FinalDimensions(inputSize);
		
		long[] paddedSize=new long[inputSize.length];
		long[] fftSize=new long[inputSize.length];
		
		FFTMethodsJTransform.dimensionsRealToComplexSmall(dim, paddedSize, fftSize);
		
		return fftSize;
	
	}
	
	@Override
	protected Img<ComplexFloatType> createFftImg(ImgFactory<T> factory, long[] size) {
		
		try {
			return factory.imgFactory(new ComplexFloatType()).create(size, new ComplexFloatType());
		}
		// TODO: error handling?
		catch (IncompatibleTypeException e) {
			return null;
		}
	}
	
	@Override
	public Img<ComplexFloatType> safeCompute(I input, Img<ComplexFloatType> output) {
		
		// TODO: proper use of Executor service
		final ExecutorService service = Executors.newFixedThreadPool(4);
		
		// TODO: Extend input to padded size using a View 
		
		FFTMethodsJTransform.realToComplex( input, output, 0, false, service );

		for ( int d = 1; d < input.numDimensions(); ++d )
			FFTMethodsJTransform.complexToComplex( output, d, true, false, service );
		 
		return output;
	}
}
