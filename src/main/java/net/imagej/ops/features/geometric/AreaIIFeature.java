package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.statistics.geometric.GeometricStatOps.Area;
import net.imglib2.IterableInterval;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Op.class, name = Area.NAME, label = Area.NAME)
public class AreaIIFeature implements AreaFeature {

	@Parameter
	private IterableInterval<?> input;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public void run() {
		out = input.size();
	}

	@Override
	public double getFeatureValue() {
		return out;
	}

}
