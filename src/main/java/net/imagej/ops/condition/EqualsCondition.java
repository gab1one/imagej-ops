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
	public static void main( String[] args)
	{
		Context ctx = new Context();
		OpService op = ctx.service(OpService.class);
		double input = 5.0;
		Condition<?> c1 = (Condition<?>) op.op("equals", 6f, input);
		Condition<?> c2 = (Condition<?>) op.op("equals", 5, input);
		Boolean result = (Boolean) op.run("and", 5.0, c1, c2);
		System.out.println(result);
	}
//(a == b) && c


}
