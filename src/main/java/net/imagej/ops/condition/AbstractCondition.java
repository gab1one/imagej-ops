package net.imagej.ops.condition;

import net.imagej.ops.AbstractFunction;


public abstract class AbstractCondition<T> extends AbstractFunction<T, Boolean> implements Condition<T> {

	@Override
	public Boolean compute(T input, Boolean output) {
		return isTrue(input);
	}

}
