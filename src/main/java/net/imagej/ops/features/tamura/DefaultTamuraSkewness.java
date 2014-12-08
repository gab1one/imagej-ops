
package net.imagej.ops.features.tamura;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.features.firstorder.FirstOrderFeatures;
import net.imagej.ops.statistics.tamura.TamuraOps;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the
 * {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraSkewness} of a
 * {@link net.imglib2.IterableInterval }
 */

@Plugin(type = Op.class, label = TamuraOps.TamuraSkewness.LABEL,
	name = TamuraOps.TamuraSkewness.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraSkewness implements
	TamuraFeatures.TamuraGranularityFeature
{

	@Parameter(type = ItemIO.INPUT)
	private TamuraFeatures.TamuraDirectionalityFeature directionality;

	@Parameter(type = ItemIO.INPUT)
	private OpService ops;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public void run() {
		out =
			(Double) ops.run(FirstOrderFeatures.SkewnessFeature.class, directionality
				.getFeatureValue());
	}

	@Override
	public double getFeatureValue() {
		return out;
	}
}
