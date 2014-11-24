
package net.imagej.ops.features.tamura;

import net.imagej.ops.Op;
import net.imagej.ops.features.tamura.helper.DefaultTamuraMaxDirectionalityHelper;
import net.imagej.ops.statistics.tamura.TamuraOps;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the
 * {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraMaxDirectionality} of
 * a {@link net.imglib2.IterableInterval }
 */

@Plugin(type = Op.class, label = TamuraOps.TamuraMaxDirectionality.LABEL,
	name = TamuraOps.TamuraMaxDirectionality.NAME,
	priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraMaxDirectionality implements
	TamuraFeatures.TamuraGranularityFeature
{

	@Parameter(type = ItemIO.INPUT)
	private DefaultTamuraMaxDirectionalityHelper maxDirectionalityHelpery;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public void run() {
		out = maxDirectionalityHelpery.getFeatureValue();
	}

	@Override
	public double getFeatureValue() {
		return out;
	}
}
