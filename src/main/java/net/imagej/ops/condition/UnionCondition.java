package net.imagej.ops.condition;

import java.awt.List;
import java.util.ArrayList;

import net.imagej.ops.Function;
import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "union")
public class UnionCondition<T> extends AbstractCondition<Object> {
	
	

	@Parameter
	private Condition<T> condition;
	
	@Parameter
	List<Condition<T>> conditions;
	
	@Parameter
	private long[] input;
	
	@Override
	public boolean isTrue(Object val) {
		
		if (conditions.size() == 0)
			throw new IllegalArgumentException("no conditions provided");
			else if (conditions.size() == 1)
			condition = (Condition<T>) conditions.get(0);
			else {
			OrCondition<T> or =
			new OrCondition<T>(conditions.get(0), conditions.get(1));
			for (int i = 2; i < conditions.size(); i++)
			or = new OrCondition<T>(or, conditions.get(i));
			condition = or;
			}
		return condition.isTrue((T) val);
	}

}
