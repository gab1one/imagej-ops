package net.imagej.ops.features.tamura;

import net.imagej.ops.Op;
import net.imagej.ops.statistics.tamura.TamuraOps;
import net.imglib2.IterableInterval;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Calculates the {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraContrast} of a {@link IterableInterval }
 * Created by gabriel on 20/11/14.
 */

@Plugin(type = Op.class, label = TamuraOps.TamuraContrast.LABEL, name = TamuraOps.TamuraContrast.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraContrast
        implements TamuraFeatures.TamuraGranularityFeature {

    @Override public double getFeatureValue() {
        return 0;
    }

    @Override public void run() {

    }
}
