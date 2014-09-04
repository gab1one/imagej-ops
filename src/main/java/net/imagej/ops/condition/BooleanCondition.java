package net.imagej.ops.condition;

import net.imagej.ops.Op;

import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "boolean")
public class BooleanCondition extends AbstractCondition<Boolean> {

	@Override
	public boolean isTrue(Boolean val) {
		return val;
	}

}
