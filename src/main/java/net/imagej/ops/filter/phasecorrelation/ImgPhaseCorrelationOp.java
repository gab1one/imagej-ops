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
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.Filter.PhaseCorrelate.class, name = Ops.Filter.PhaseCorrelate.NAME)
public class ImgPhaseCorrelationOp<T extends RealType<T>, C extends ComplexType<C>>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, AffineGet>implements PhaseCorrelate {

	@Parameter(type = ItemIO.INPUT)
	private RandomAccessibleInterval<T> interval1;

	@Parameter(type = ItemIO.INPUT)
	private float normalizationThreshold;

	@Parameter(type = ItemIO.INPUT, required = false)
	private int numPeaks = 5;

	@Parameter
	private OpService ops;

	@Override
	public AffineGet compute(RandomAccessibleInterval<T> input) {
		
		// TODO Fix type exception: ComplexFloatType can't be cast to RealType
				
		Img<C> out1 = ops.create().img(input);
		Img<C> out2 = ops.create().img(interval1);
						
		// calculate inverse FFT
		RandomAccessibleInterval<C> fft1 = ops.filter().fft(out1, input);
		RandomAccessibleInterval<C> fft2 = ops.filter().fft(out2, interval1);
		
		// call normalizeComplexImgOp 
		RandomAccessibleInterval<C> img1 = ops.image().complexNormalize(fft1, normalizationThreshold);
		RandomAccessibleInterval<C> img2 = ops.image().complexNormalizeConjugate(fft2, normalizationThreshold);
		
		Cursor<C> fft1cursor = Views.flatIterable(img1).cursor();
		Cursor<C> fft2cursor = Views.flatIterable(img2).cursor();		
		
		while (fft1cursor.hasNext()) {
            fft1cursor.next().mul(fft2cursor.next());
        }
		
		// maybe create new output instance instead of using img2 as output
		ops.filter().ifft(img1, interval1);
		
		List<PhaseCorrelationPeak> peakList = extractPeaks(interval1, numPeaks);
		
		PhaseCorrelationPeak topPeak = peakList.get(peakList.size() - 1);
		
		long[] peakPosition = topPeak.getPosition();
		int size = peakPosition.length;
		
		double[][] translationArray = new double[size][size];
		for (int i = 0; i <= size; i++ ){
			for (int j = 0; j <= size; j++) {
				if ( i == j) {
					translationArray[i][j] = 1;
				} else if (j == size) {
					translationArray[i][j] = peakPosition[i];
				} else {
					translationArray[i][j] = 0;
				}
			}
		}
		
		Matrix test = new Matrix(translationArray);
		
		AffineGet affineTransformation = new AffineTransform(test);
		
		return affineTransformation; // return result
	}

	private List<PhaseCorrelationPeak> extractPeaks(final RandomAccessibleInterval<T> invPCM, final int numPeaks) {
		FixedSizePriorityQueue<PhaseCorrelationPeak> peaks = new FixedSizePriorityQueue<PhaseCorrelationPeak>(numPeaks);
		final int dims = invPCM.numDimensions();

		ExtendedRandomAccessibleInterval<T, RandomAccessibleInterval<T>> extended = Views.extendZero(invPCM);
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
			PhaseCorrelationPeak peak = new PhaseCorrelationPeak(maxPos, (float) maxValue);
			peak.setOriginalInvPCMPosition(maxPos);
			peaks.add(peak);
		}
		return peaks.asList();
	}

}
