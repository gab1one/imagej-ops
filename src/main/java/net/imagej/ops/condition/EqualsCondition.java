package net.imagej.ops.condition;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;

import org.scijava.Context;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "equals")
public class EqualsCondition extends AbstractCondition<Object> {

	@Parameter
	private Object o;
	
	@Override
	public boolean isTrue(Object val) {
		return o.equals(val);
	}

}
