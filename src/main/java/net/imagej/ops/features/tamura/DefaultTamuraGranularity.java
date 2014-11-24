
package net.imagej.ops.features.tamura;

import net.imagej.ops.Op;
import net.imagej.ops.statistics.tamura.TamuraOps;
import net.imagej.ops.statistics.tamura.TamuraOps.TamuraGranularity;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Calculates the {@link TamuraGranularity} of a {@link Iterable} Created by
 * gabriel on 20/11/14. TODO find out what
 */
@Plugin(type = Op.class, label = TamuraGranularity.LABEL,
	name = TamuraOps.TamuraGranularity.NAME,
	priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraGranularity implements
	TamuraFeatures.TamuraGranularityFeature
{

	@Override
	public void run() {

	}

	@Override
	public double getFeatureValue() {
		return 0;
	}
}
