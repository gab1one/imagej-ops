package net.imagej.ops.features.tamura;

import net.imagej.ops.Op;
import net.imagej.ops.statistics.tamura.TamuraOps;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Calculates the {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraContrast} of a {@link net.imglib2.IterableInterval }
 */

@Plugin(type = Op.class, label = TamuraOps.TamuraContrast.LABEL, name = TamuraOps.TamuraContrast.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraDirectionality implements TamuraFeatures.TamuraDirectionalityFeature {
    @Override public double getFeatureValue() {
        return 0;
    }

    @Override public void run() {

    }
}
