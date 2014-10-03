
package net.imagej.ops.fft.image;

import org.scijava.plugin.Parameter;

import net.imagej.ops.Ops.Fft;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;

public abstract class AbstractFftImage<T, I extends Img<T>, C, O extends Img<C>> extends
	AbstractFftIterable<T, C, I, O> implements Fft
{
	@Parameter(required=false)
	Boolean fast=true;

	@Override
	public O createOutput(I input) {
		
		long[] inputSize=new long[input.numDimensions()];
		
		for (int d=0;d<input.numDimensions();d++) {
			inputSize[d]=input.dimension(d);
		}
		
		long[] size;
		
		if (fast) {
			size=getFftFastSize(inputSize);
		}
		else {
			size=getFftSmallSize(inputSize);
		}
		
		return createFftImg(input.factory(), size);

	}
	
	protected abstract long[] getFftFastSize(long[] inputSize);
	protected abstract long[] getFftSmallSize(long[] inputSize);
	protected abstract O createFftImg(ImgFactory<T> factory, long[] size);

}
