package net.imagej.ops.filter.phasecorrelation;

import net.imagej.ImgPlus;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.SCIFIOImgPlus;


public class PhaseCorrelateTest extends AbstractOpTest {

	/** 'Test' the phaseCorrelate op 
	 * @param <T>
	 * @throws ImgIOException */
	@Test
	public <T extends RealType<T>> void phaseCorrelateTest() throws ImgIOException {

		Img<FloatType> in = generateFloatArrayTestImg(true, new long[] {10,10});
		Img<FloatType> in2 = generateFloatArrayTestImg(true, new long[] {10,10});
		AffineGet out1;
		final float threshold = 1E-5f;
		
		ImgOpener opener = new ImgOpener();

        ImgPlus<T> sfimp1 = (SCIFIOImgPlus<T>) opener
                .openImgs("images/square0.ome.tif").get(0);


		out1 = ops.filter().phaseCorrelate(sfimp1, sfimp1, threshold);
		
		out1.toString();
		
	}
}
