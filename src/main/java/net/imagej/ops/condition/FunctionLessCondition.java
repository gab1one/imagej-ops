package net.imagej.ops.condition;

import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "function greater")
public class FunctionLessCondition extends AbstractCondition<Object> {

	@Parameter
	Object o;

	
	@Override
	public boolean isTrue(Object val) {
		int result = ((String) o).compareTo((String) val);
		
		return result < 0;
	}

}
