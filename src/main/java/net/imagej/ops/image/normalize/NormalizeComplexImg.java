package net.imagej.ops.image.normalize;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Image.ComplexNormalize;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;

// TODO: change type from Img to RandomAccessibleInterval if possible

@Plugin(type=Ops.Image.ComplexNormalize.class, name=Ops.Image.ComplexNormalize.NAME)
public class NormalizeComplexImg<T extends ComplexType<T>> extends AbstractFunctionOp<Img<T>, Img<T>> implements ComplexNormalize {

	@Parameter(type=ItemIO.INPUT)
	private float normalizationThreshold;
	
	@Override
	public Img<T> compute(Img<T> input) {		
		
		Cursor<T> cursor = input.cursor();
		
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
		}
		return input;
	}
	
	
	

}

