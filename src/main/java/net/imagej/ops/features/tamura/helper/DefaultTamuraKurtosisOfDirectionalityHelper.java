package net.imagej.ops.features.tamura.helper;

import net.imagej.ops.OpService;
import net.imagej.ops.features.Feature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures;
import net.imagej.ops.features.tamura.TamuraFeatures;
import net.imagej.ops.statistics.firstorder.FirstOrderStatOps;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Helper Feature for {@link net.imagej.ops.features.tamura.DefaultTamuraKurtosisOfDirectionality}
 */
public class DefaultTamuraKurtosisOfDirectionalityHelper implements Feature{

    @Parameter
    private TamuraFeatures.TamuraDirectionalityFeature directionality;

    @Parameter(type = ItemIO.OUTPUT)
    private double out;

    @Parameter(type = ItemIO.INPUT)
    private OpService ops;

    @Override public double getFeatureValue() {
        return out;
    }

    @Override public void run() {
        out = (Double) ops.run(FirstOrderFeatures.KurtosisFeature.class, directionality.getFeatureValue());
    }
}
