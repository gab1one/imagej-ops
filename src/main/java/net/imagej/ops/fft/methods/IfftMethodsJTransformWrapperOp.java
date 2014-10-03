
package net.imagej.ops.fft.methods;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Ifft;
import net.imagej.ops.fft.image.AbstractIfftImage;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;

/**
 * Inverse FFT op implemented by wrapping FFTMethodsJTransform.
 * 
 * @author bnorthan
 *
 * @param <T>
 * @param <I>
 */
@Plugin(type = AbstractIfftImage.class, name = Ifft.NAME)
public class IfftMethodsJTransformWrapperOp<T extends RealType<T>, O extends Img<T>>
	extends AbstractIfftImage<ComplexFloatType, Img<ComplexFloatType>, T, O>
{

	@Override
	public O compute(Img<ComplexFloatType> input, O output) {

		// TODO: proper use of Executor service
		final ExecutorService service = Executors.newFixedThreadPool(4);

		// TODO: Extend output to padded size using a View 
		
		for (int d = 1; d < input.numDimensions(); ++d)
			FFTMethodsJTransform.complexToComplex(input, d, false, true, service);

		FFTMethodsJTransform.complexToReal(input, output, 0, true, service);

		return output;
	}
}
