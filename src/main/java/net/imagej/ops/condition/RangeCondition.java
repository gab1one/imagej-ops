package net.imagej.ops.condition;

import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "range")
public class RangeCondition extends AbstractCondition<Object> {

	@Parameter
	private long first; 
	
	@Parameter
	private long last;
	
	@Parameter
	private long step;
	
	@Override
	public boolean isTrue(Object val) {
		if((Long)val < first){return false;}
		if((Long)val > last){return false;}
		
		return ((Long)val - first) % step == 0;
		
		 
	}

}
