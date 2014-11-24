package net.imagej.ops.features.tamura;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.statistics.tamura.TamuraOps;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Calculates the {@link net.imagej.ops.statistics.tamura.TamuraOps.TamuraKurtosisOfDirectionality} of a {@link net.imglib2.IterableInterval }
 * Created by gabriel on 20/11/14.
 */

@Plugin(type = Op.class, label = TamuraOps.TamuraKurtosisOfDirectionality.LABEL, name = TamuraOps.TamuraKurtosisOfDirectionality.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultTamuraKurtosisOfDirectionality extends
        AbstractOutputFunction<IterableInterval<? extends RealType<?>>, RealType<?>>
        implements TamuraFeatures.TamuraGranularityFeature

{
    @Override protected RealType<?> safeCompute(
            IterableInterval<? extends RealType<?>> input,
            RealType<?> output) {
        return null;
    }

    @Override public RealType<?> createOutput(
            IterableInterval<? extends RealType<?>> input) {
        return null;
    }

    @Override public double getFeatureValue() {
        return 0;
    }

}
