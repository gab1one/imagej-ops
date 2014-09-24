package net.imagej.ops.condition;

import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "greater_equal")
public class FunctionGreaterEqualCondition<T extends Comparable<T>> extends AbstractCondition<T> {

	@Parameter
	T o;

	@Override
	public boolean isTrue(T val) {
		int result = val.compareTo(o);
		
		//TODO:
		//Double.NaN.compareTo(o) always returns 1 but Double.NaN > o returns false. 
		//This could be confusing. Should this method return false if val is NaN?
		
		
		return result >= 0;
	}

}
