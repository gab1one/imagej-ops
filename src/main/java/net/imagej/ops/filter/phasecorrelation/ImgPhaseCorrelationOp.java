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
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.Filter.PhaseCorrelate.class, name = Ops.Filter.PhaseCorrelate.NAME)
public class ImgPhaseCorrelationOp<T extends RealType<T>, C extends ComplexType<C>>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, long[]>implements PhaseCorrelate {

	@Parameter(type = ItemIO.INPUT)
	private RandomAccessibleInterval<T> input2;

	@Parameter(type = ItemIO.INPUT)
	private float normalizationThreshold;

	@Parameter(type = ItemIO.INPUT, required = false)
	private int numPeaks = 5;

	@Parameter
	private OpService ops;

	@Override
	public long[] compute(RandomAccessibleInterval<T> input1) {

		// calculate FFT
		@SuppressWarnings("unchecked")
		RandomAccessibleInterval<C> fft1 = (RandomAccessibleInterval<C>) ops.filter().fft(null, input1, null, true);
		@SuppressWarnings("unchecked")
		RandomAccessibleInterval<C> fft2 = (RandomAccessibleInterval<C>) ops.filter().fft(null, input2, null, true);

		// normalize fft imgs
		ops.image().complexNormalize(fft1, normalizationThreshold);
		ops.image().complexNormalizeConjugate(fft2, normalizationThreshold);

		Cursor<C> fft1cursor = Views.flatIterable(fft1).cursor();
		Cursor<C> fft2cursor = Views.flatIterable(fft2).cursor();

		while (fft1cursor.hasNext()) {
			fft1cursor.next().mul(fft2cursor.next());
		}

		ops.filter().ifft(input1, fft1);

		List<PhaseCorrelationPeak> peakList = extractPeaks(input1, numPeaks);

		PhaseCorrelationPeak topPeak = peakList.get(peakList.size() - 1);

		long[] peakPosition = topPeak.getPosition();
		
		return peakPosition;
	}

	private List<PhaseCorrelationPeak> extractPeaks(final RandomAccessibleInterval<T> invPCM, final int numPeaks) {
		FixedSizePriorityQueue<PhaseCorrelationPeak> peaks = new FixedSizePriorityQueue<PhaseCorrelationPeak>(numPeaks);
		final int dims = invPCM.numDimensions();

		ExtendedRandomAccessibleInterval<T, RandomAccessibleInterval<T>> extended = Views.extendZero(invPCM);
		IntervalView<T> interval = Views.interval(extended, invPCM);

		// Define neighborhood for the Peaks
		final int neighborhoodSize = 3; 
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
			PhaseCorrelationPeak peak = new PhaseCorrelationPeak(maxPos,  maxValue);
			peak.setOriginalInvPCMPosition(maxPos);
			peaks.add(peak);
		}
		return peaks.asList();
	}

}
