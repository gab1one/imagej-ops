
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
 * {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraStdDevDirectionality}
 * of a {@link net.imglib2.IterableInterval } Created by gabriel on 20/11/14.
 */

@Plugin(type = Op.class, label = TamuraOps.TamuraStdDevDirectionality.LABEL,
	name = TamuraOps.TamuraStdDevDirectionality.NAME,
	priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraStdDevDirectionality implements
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
			(Double) ops.run(FirstOrderFeatures.StdDeviationFeature.class,
				directionality.getFeatureValue());
	}

	@Override
	public double getFeatureValue() {
		return out;
	}

}
