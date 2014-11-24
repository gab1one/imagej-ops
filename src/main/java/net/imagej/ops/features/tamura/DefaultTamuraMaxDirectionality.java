package net.imagej.ops.features.tamura;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.statistics.tamura.TamuraOps;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;



/**
 * Calculates the {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraMaxDirectionality} of a {@link net.imglib2.IterableInterval }
 */

@Plugin(type = Op.class, label = TamuraOps.TamuraMaxDirectionality.LABEL, name = TamuraOps.TamuraMaxDirectionality.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraMaxDirectionality
        implements TamuraFeatures.TamuraGranularityFeature {

    @Override public double getFeatureValue() {
        return 0;
    }

    @Override public void run() {

    }
}

