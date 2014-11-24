
package net.imagej.ops.features.tamura;

import net.imagej.ops.Op;
import net.imagej.ops.features.tamura.helper.DefaultTamuraKurtosisOfDirectionalityHelper;
import net.imagej.ops.statistics.tamura.TamuraOps;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the
 * {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraKurtosisOfDirectionality}
 * of a {@link net.imglib2.IterableInterval } Created by gabriel on 20/11/14.
 */

@Plugin(type = Op.class,
	label = TamuraOps.TamuraKurtosisOfDirectionality.LABEL,
	name = TamuraOps.TamuraKurtosisOfDirectionality.NAME,
	priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraKurtosisOfDirectionality implements
	TamuraFeatures.TamuraGranularityFeature
{

	@Parameter(type = ItemIO.INPUT)
	private DefaultTamuraKurtosisOfDirectionalityHelper kurtosisOfDirectionality;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public void run() {
		out = kurtosisOfDirectionality.getFeatureValue();
	}

	@Override
	public double getFeatureValue() {
		return out;
	}
}
