package net.imagej.ops.condition;


import java.util.ArrayList;


import net.imagej.ops.Op; 

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "dimension equals")

public class DimensionEqualCondition<T> extends AbstractCondition<Object> {


	@Parameter
	private int index;

	@Parameter
	ArrayList<T> listing;

	@Override
	public boolean isTrue(Object val) {
		if (listing.get(index).equals(val))
		{
			return true;
		}
		return false; 

	}

}
