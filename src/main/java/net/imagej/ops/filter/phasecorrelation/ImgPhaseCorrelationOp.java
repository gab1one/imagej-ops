package net.imagej.ops.filter.phasecorrelation;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.PhaseCorrelate;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

@Plugin(type=Ops.Filter.PhaseCorrelate.class, name=Ops.Filter.PhaseCorrelate.NAME)
public class ImgPhaseCorrelationOp<T extends RealType<T>> extends AbstractFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements PhaseCorrelate {

	@Parameter(type=ItemIO.INPUT)
	private RandomAccessibleInterval<T> interval1;
	
	@Parameter(type=ItemIO.INPUT, required=false)
	private int test;
	
	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> input) {
		
		
		// TODO Auto-generated method stub
		
		
		return null; // return result
	}
	
	
	

}
