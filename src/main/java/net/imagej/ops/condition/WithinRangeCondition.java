package net.imagej.ops.condition;

import net.imagej.ops.Function;
import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "function greater")
public class WithinRangeCondition<T> extends AbstractCondition<Object> {
	
	@Parameter
	private Function<long[],T> valueFunc;
	
	@Parameter
	private double min;
	
	@Parameter
	private double max;
	
	@Parameter
	private long[] input;
	
	@Override
	public boolean isTrue(Object val) {
		 T tmp = (T) val; 
		
		 valueFunc.compute(input, tmp);
		 
		 return (min <= (Double)val) && ((Double)val <= max);
	}

}
