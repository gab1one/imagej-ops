package net.imagej.ops.features;

import net.imagej.ops.Op;

/**
 * A {@link Feature} as an {@link Op} with an 'double' output. This interface is
 * a marker for {@link Op}s which can be consumed by a {@link FeatureService}.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public interface Feature extends Op {

	/**
	 * @return the calculated feature value
	 */
	double getFeatureValue();
}
