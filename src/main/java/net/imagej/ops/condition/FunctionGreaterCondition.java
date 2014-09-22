package net.imagej.ops.condition;

import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "greater")
public class FunctionGreaterCondition<T extends Comparable<T>> extends AbstractCondition<T> {

	@Parameter
	T o;

	@Override
	public boolean isTrue(T val) {
		int result = val.compareTo(o);
		
		return result > 0;
	}

}
