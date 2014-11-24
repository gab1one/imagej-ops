package net.imagej.ops.features.tamura.helper;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.features.Feature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures;
import net.imagej.ops.features.tamura.TamuraFeatures;
import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Helper Feature for {@link net.imagej.ops.features.tamura.DefaultTamuraKurtosisOfDirectionality}
 */
@Plugin(type = Op.class, name = "defaulttamurakurtosisofdirectionality", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraKurtosisOfDirectionalityHelper implements Feature{

    @Parameter(type = ItemIO.INPUT)
    private TamuraFeatures.TamuraDirectionalityFeature directionality;

    @Parameter(type = ItemIO.INPUT)
    private OpService ops;

    @Parameter(type = ItemIO.OUTPUT)
    private double out;

    @Override public double getFeatureValue() {
        return out;
    }

    @Override public void run() {
        out = (Double) ops.run(FirstOrderFeatures.KurtosisFeature.class, directionality.getFeatureValue());
    }
}
