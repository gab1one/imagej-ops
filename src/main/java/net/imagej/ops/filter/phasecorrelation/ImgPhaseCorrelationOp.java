package net.imagej.ops.filter.phasecorrelation;

import java.util.List;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import Jama.Matrix;
import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.PhaseCorrelate;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.Filter.PhaseCorrelate.class, name = Ops.Filter.PhaseCorrelate.NAME)
public class ImgPhaseCorrelationOp<T extends RealType<T>, C extends ComplexType<C>>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, AffineGet>implements PhaseCorrelate {

	@Parameter(type = ItemIO.INPUT)
	private RandomAccessibleInterval<T> input2;

	@Parameter(type = ItemIO.INPUT)
	private float normalizationThreshold;

	@Parameter(type = ItemIO.INPUT, required = false)
	private int numPeaks = 5;

	@Parameter
	private OpService ops;

	@Override
	public AffineGet compute(RandomAccessibleInterval<T> input1) {

		// calculate FFT
		RandomAccessibleInterval<C> fft1 = (RandomAccessibleInterval<C>) ops.filter().fft(null, input1, null, true);
		RandomAccessibleInterval<C> fft2 = (RandomAccessibleInterval<C>) ops.filter().fft(null, input2, null, true);

		// normalize fft imgs
		ops.image().complexNormalize(fft1, normalizationThreshold);
		ops.image().complexNormalizeConjugate(fft2, normalizationThreshold);

		//TODO put in function
		Cursor<C> fft1cursor = Views.flatIterable(fft1).cursor();
		Cursor<C> fft2cursor = Views.flatIterable(fft2).cursor();

		while (fft1cursor.hasNext()) {
			fft1cursor.next().mul(fft2cursor.next());
		}

		ops.filter().ifft(input1, fft1);

		// TODO extract method \/
		List<PhaseCorrelationPeak> peakList = extractPeaks(input1, numPeaks);

		PhaseCorrelationPeak topPeak = peakList.get(peakList.size() - 1);

		long[] peakPosition = topPeak.getPosition();
		int size = peakPosition.length;
		if (size < 3) {
			size = 3;
		}

		// fill translation matrix
		double[][] translationArray = new double[size][size + 1];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j <= size; j++) {
				if (i == j) {
					translationArray[i][j] = 1;
				} else if (j == size) {
					if (peakPosition.length <= i) {
						translationArray[i][j] = 0;
					} else {
						translationArray[i][j] = peakPosition[i];
					}
				} else {
					translationArray[i][j] = 0;
				}
			}
		}

		Matrix matrix = new Matrix(translationArray);
		AffineGet affineTransformation = new AffineTransform(matrix);

		return affineTransformation; // return result
	}

	private List<PhaseCorrelationPeak> extractPeaks(final RandomAccessibleInterval<T> invPCM, final int numPeaks) {
		FixedSizePriorityQueue<PhaseCorrelationPeak> peaks = new FixedSizePriorityQueue<PhaseCorrelationPeak>(numPeaks);
		final int dims = invPCM.numDimensions();

		ExtendedRandomAccessibleInterval<T, RandomAccessibleInterval<T>> extended = Views.extendZero(invPCM);
		IntervalView<T> interval = Views.interval(extended, invPCM);

		// Define neighborhood for the Peaks
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
			PhaseCorrelationPeak peak = new PhaseCorrelationPeak(maxPos,  maxValue);
			peak.setOriginalInvPCMPosition(maxPos);
			peaks.add(peak);
		}
		return peaks.asList();
	}

}
