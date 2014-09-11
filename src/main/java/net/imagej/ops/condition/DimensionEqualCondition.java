package net.imagej.ops.condition;

import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "dimension equals")
public class DimensionEqualCondition extends AbstractCondition<Object> {

	@Parameter
	private int index;

	@Parameter
	private Object[] list;

	@Override
	public boolean isTrue(Object val) {
		return list[index] == val;
	}

}
