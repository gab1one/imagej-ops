package net.imagej.ops.condition;

import net.imagej.ops.Op;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = And.NAME)
public class OrCondition<T> extends AbstractCondition<T> implements And {

	@Parameter
	private Condition<T> c1;

	@Parameter
	private Condition<T> c2;

	@Override
	public boolean isTrue(T val) {
		return c1.isTrue(val) || c2.isTrue(val);
	}

}
