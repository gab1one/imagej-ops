/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imagej.ops.create.kernel;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.AbstractCreateKernelImg;
import net.imglib2.Cursor;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This Op takes a 2-D array of explicit values and creates a simple image, e.g.
 * for use as a kernel in convolution.
 *
 * @author Mark Hiner hinerm at gmail.com
 */
@Plugin(type = Ops.Create.Kernel.class)
public class CreateKernel<T extends ComplexType<T> & NativeType<T>>
		extends AbstractCreateKernelImg<T> implements Ops.Create.Kernel, Contingent {

	@Parameter
	private double[][] values;

	@Override
	public void run() {
		final long[] dims = {values.length, values[0].length};
		createOutputImg(dims);

		final Cursor<T> cursor = getOutput().cursor();
			for (int j = 0; j < values.length; j++) {
				for (int k = 0; k < values[j].length; k++) {
					cursor.fwd();

					cursor.get().setReal(values[j][k]);
				}
			}
	}

	@Override
	public boolean conforms() {
		// check the nested arrays for nulls
		for (int i=0; i<values.length; i++) if (values[i] == null) return false;
		return true;
	}
}
