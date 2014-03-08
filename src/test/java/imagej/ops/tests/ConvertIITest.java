/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.tests;

import static org.junit.Assert.assertTrue;
import imagej.ops.OpService;
import imagej.ops.convert.ConvertII;
import imagej.ops.convert.ConvertPixCopy;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.ShortType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;

/**
 * A test of {@link ConvertII}.
 * 
 * @author Martin Horn
 */
public class ConvertIITest {

	private Context context;
	private OpService ops;

	@Before
	public void setUp() {
		context = new Context(OpService.class);
		ops = context.getService(OpService.class);
		assertTrue(ops != null);
	}

	@After
	public synchronized void cleanUp() {
		if (context != null) {
			context.dispose();
			context = null;
		}
	}

	@Test
	public void test() throws IncompatibleTypeException {

		Img<ShortType> img =
			new ArrayImgFactory<ShortType>().create(new int[] { 10, 10 },
				new ShortType());
		Img<ByteType> res =
			img.factory().imgFactory(new ByteType()).create(img, new ByteType());

		// TODO won't work for now, as UnaryFunction's out is ItemIO.OUTPUT
		ops.run("convert", img, new ConvertPixCopy<ShortType, ByteType>(), res);

		// TODO won't work neither, as the pre-processor to create the result is
		// missing
		ops.run("convert", img, new ConvertPixCopy<ShortType, ByteType>());

	}
}