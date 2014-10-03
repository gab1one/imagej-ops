package net.imagej.ops.condition;

import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "range")
public class WithinRangeCondition<T extends Comparable<T>> extends AbstractCondition<T> {

	@Parameter
	T max;
	@Parameter
	T min;

	@Override
	public boolean isTrue(T val) {
		

		return (min.compareTo(val) <= 0 ) && (max.compareTo(val) >= 0 );
	}

}
