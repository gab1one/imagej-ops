package net.imagej.ops.condition;

import net.imagej.ops.Op;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Not.NAME)
public class NotCondition<T> extends AbstractCondition<T> implements And {

	@Parameter
	private Condition<T> c1;

	@Override
	public boolean isTrue(T val) {
		return !c1.isTrue(val);
	}

}
