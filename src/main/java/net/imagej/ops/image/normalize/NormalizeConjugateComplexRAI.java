package net.imagej.ops.image.normalize;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Image.ComplexNormalizeConjugate;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.view.Views;


@Plugin(type=Ops.Image.ComplexNormalizeConjugate.class, name=Ops.Image.ComplexNormalizeConjugate.NAME)
public class NormalizeConjugateComplexRAI<T extends ComplexType<T>> extends AbstractFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements ComplexNormalizeConjugate {

	@Parameter(type=ItemIO.INPUT)
	private float normalizationThreshold;
	
	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> input) {
		
		Cursor<T> cursor = Views.flatIterable(input).cursor();
		
		while(cursor.hasNext()) {
			cursor.next();
			ComplexType<T> value = cursor.get();
			
			final float real = cursor.get().getRealFloat();
			final float complex = cursor.get().getImaginaryFloat();
			
			final float length = (float) Math.sqrt(real * real + complex * complex);

	        if (length < normalizationThreshold) {
	            value.setReal(0);
	            value.setImaginary(0);
	        } else {
	            value.setReal(real / length);
	            value.setImaginary(complex / length);
	        }
	        cursor.get().complexConjugate();
		}
		return input;
	}

}
