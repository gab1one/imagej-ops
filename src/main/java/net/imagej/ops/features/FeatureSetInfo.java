package net.imagej.ops.features;

import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OutputFunction;

/**
 * A {@link FeatureSetInfo} describes {@link Set}s of {@link Feature}s which
 * have some common property. A typical example the FirstOrderStatistics or
 * HaralickFeatures.
 * 
 * Given some input <I>, the {@link FeatureService} creates an updateable
 * {@link OutputFunction} to actually calculate the set of {@link Feature}s. See
 * {@link FeatureService} for details.
 * 
 * {@link Feature}s of a {@link FeatureSetInfo} can be enabled/disabled.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public interface FeatureSetInfo {

	/**
	 * @return the set of all {@link Feature}s which shall be calculated.
	 */
	Set<Class<? extends Feature>> getEnabledFeatures();

	/**
	 * @return additional {@link Op}s required to calculate the {@link Feature}
	 *         s. TODO: We maybe can delete this method in the future.
	 */
	Set<Class<? extends Op>> getAdditionalOps();

	/**
	 * Disables a {@link Feature}. This {@link Feature} will not be calculated.
	 */
	void disableFeature(Class<? extends Feature> feature);

	/**
	 * Enabled a {@link Feature}. This {@link Feature} will be calculated.
	 */
	void enableFeature(Class<? extends Feature> feature);

	/**
	 * Allows joining two {@link FeatureSetInfo}s.
	 * 
	 * @return the joined {@link FeatureSetInfo}
	 */
	FeatureSetInfo join(final FeatureSetInfo other);

}
