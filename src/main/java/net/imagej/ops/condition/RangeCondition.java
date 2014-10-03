package net.imagej.ops.condition;

import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "range")
public class RangeCondition<T extends Comparable<T>> extends AbstractCondition<T> {

	@Parameter
	T first;
	@Parameter
	T last;
	@Parameter
	T step;

	@Override
	public boolean isTrue(T val) {
		if(val.compareTo(first) < 0 ){return false;}
		
		if(val.compareTo(last) > 0 ){return false;}
		
		
		//TODO:
		//Double.NaN.compareTo(o) always returns 1 but Double.NaN > o returns false. 
		//This could be confusing. Should this method return false if val is NaN?
		
		return (true) ;
	}

}
