package net.imagej.ops.filter.phasecorrelation;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;


public class PhaseCorrelateTest extends AbstractOpTest {

	/** 'Test' the phaseCorrelate op */
	@Test
	public void phaseCorrelateTest() {

		Img<FloatType> in = generateFloatArrayTestImg(true, new long[] {10,10});
		Img<FloatType> in2 = generateFloatArrayTestImg(true, new long[] {10,10});
		AffineGet out1;
		final float threshold = 1E-5f;


		out1 = ops.filter().phaseCorrelate(in, in2, threshold);
		
		out1.toString();
		
	}
}
