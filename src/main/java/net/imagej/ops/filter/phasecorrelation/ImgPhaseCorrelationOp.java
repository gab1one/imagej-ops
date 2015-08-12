package net.imagej.ops.filter.phasecorrelation;

import java.util.List;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.PhaseCorrelate;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type=Ops.Filter.PhaseCorrelate.class, name=Ops.Filter.PhaseCorrelate.NAME)
public class ImgPhaseCorrelationOp<T extends RealType<T>> extends AbstractFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements PhaseCorrelate {

	@Parameter(type=ItemIO.INPUT)
	private RandomAccessibleInterval<T> interval1;
	
	@Parameter(type=ItemIO.INPUT)
	private float normalizationThreshold;
	
	@Parameter(type=ItemIO.INPUT, required=false)
	private int numPeaks = 5;
	
	@Parameter
	private OpService ops;
	
	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> input) {
				
		// calculate inverse FFT
		RandomAccessibleInterval<T> fft1 = (RandomAccessibleInterval<T>) ops.filter().fft(input);
		RandomAccessibleInterval<T> fft2 = (RandomAccessibleInterval<T>) ops.filter().fft(interval1);
		
		// call normalizeComplexImgOp 
		RandomAccessibleInterval<T> img1 = ops.image().normalize(fft1, normalizationThreshold);
		RandomAccessibleInterval<T> img2 = ops.image().normalizeConjugate(fft2, normalizationThreshold);
		
		Cursor<T> fft1cursor = Views.flatIterable(fft1).cursor();
		Cursor<T> fft2cursor = Views.flatIterable(fft2).cursor();		
		
		while (fft1cursor.hasNext()) {
            fft1cursor.next().mul(fft2cursor.next());
        }
		
		// maybe create new output instance instead of using img2 as output
		ops.filter().ifft(img2, img1);
		
		List<PhaseCorrelationPeak> peakList = extractPeaks(img2, numPeaks);
		
		PhaseCorrelationPeak topPeak = peakList.get(peakList.size() - 1);
			
		// TODO use AffineTransform as output
		
		return img2; // return result
	}
	
	private List<PhaseCorrelationPeak> extractPeaks(final RandomAccessibleInterval<T> invPCM, final int numPeaks) {
		FixedSizePriorityQueue<PhaseCorrelationPeak> peaks =
                new FixedSizePriorityQueue<PhaseCorrelationPeak>(numPeaks);
        final int dims = invPCM.numDimensions();

        ExtendedRandomAccessibleInterval<T, RandomAccessibleInterval<T>> extended =
                Views.extendZero(invPCM);
        IntervalView<T> interval = Views.interval(extended, invPCM);

        // TODO: OFFSETS?

        // Define neightborhood for the Peaks
        final int neighborhoodSize = 3; // TODO
        RectangleShape rs = new RectangleShape(neighborhoodSize, false);
        Cursor<Neighborhood<T>> neighbour = rs.neighborhoods(interval).cursor();
        
        // find local maximum in each neighborhood
        while (neighbour.hasNext()) {
            Cursor<T> nhCursor = neighbour.next().localizingCursor();
            double maxValue = 0.0d;
            long[] maxPos = new long[dims];
            
            while (nhCursor.hasNext()) {
                double localValue = nhCursor.next().getRealDouble();
                
                if (localValue > maxValue) {
                    maxValue = localValue;
                    nhCursor.localize(maxPos);
                }
            }
            // queue ensures only n best are added.
            // FIXME cast to float
            PhaseCorrelationPeak peak =
                    new PhaseCorrelationPeak(maxPos, (float) maxValue);
            peak.setOriginalInvPCMPosition(maxPos);
            peaks.add(peak);
        }
        return peaks.asList();
	}
	
	
	

}
