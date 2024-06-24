import java.util.Arrays;

public class ConditionalAggregationOperators {

	private AuxiliaryMethods am = new AuxiliaryMethods();
	private Measures measures = new Measures();
	private ChoquetIntegral chi = new ChoquetIntegral();

	public double sumAO(double[] vector, int[] set) {
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			result = result + vector[set[i]];
		}
		return result;
	}

	// OUR AO - averaging type of aggregation frac{1}{|E|}\cdot\sum_{i\in E} x_i
	public double normSumAO(double[] vector, int[] set) {
		if (set.length == 0) {
			return 0;
		}
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			result = (1.0 / (double) set.length) * (sumAO(vector, set));
		}
		return result;
	}

	public double pMeanAO(double[] vector, int[] set, double p) {
		if (set.length == 0) {
			return 0;
		}
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			result = result + Math.pow(vector[set[i]], p);
		}
		result = result / ((double) set.length);
		result = Math.pow(result, 1.0 / p);
		return result;
	}

	public double meanAO(double[] vector, int[] set) {
		return pMeanAO(vector, set, 1.0);
	}

	public double maximumAO(double[] vector, int[] set) {
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			if (result < vector[set[i]]) {
				result = vector[set[i]];
			}
		}
		return result;
	}

	public double minimumAO(double[] vector, int[] set) {
		double result = Double.MAX_VALUE;
		for (int i = 0; i < set.length; i++) {
			if (result > vector[set[i]]) {
				result = vector[set[i]];
			}
		}
		return result;
	}

	public double maxMinAO(double[] vector, int[] set) {
		double max = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < set.length; i++) {
			if (max < vector[set[i]]) {
				max = vector[set[i]];
			}
			if (min > vector[set[i]]) {
				min = vector[set[i]];
			}
		}
		return ((max + min) / 2);
	}
	

	public double minMaxAO(double[] vector, int[] set) {
		double max = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < set.length; i++) {
			if (max < vector[set[i]]) {
				max = vector[set[i]];
			}
			if (min > vector[set[i]]) {
				min = vector[set[i]];
			}
		}
		return Math.max(normSumAO(vector, set), max - min);
	}

	public double ChoquetAO(double[] vector, int[] set, double[] measure) {
		double[] newVector = new double[vector.length];
		for (int i = 0; i < set.length; i++) {
			newVector[set[i]] = vector[set[i]];
		}
		return chi.CHI(newVector, measure);
	}

	public double ChoquetAOUniformMeasure(double[] vector, int[] set, double q) {
		double[] newVector = new double[vector.length];
		for (int i = 0; i < set.length; i++) {
			newVector[set[i]] = vector[set[i]];
		}
		double[] newVectorIncreasing = am.increasingArrange(newVector);
		double result = newVectorIncreasing[0];
		double n = newVectorIncreasing.length;
		for (int i = 1; i < n; i++) {
			if (n - i>=set.length) {
				result = result + ((newVectorIncreasing[i] - newVectorIncreasing[i - 1]) * 1);
			} else {
				result = result + ((newVectorIncreasing[i] - newVectorIncreasing[i - 1]) * (Math.pow((n - i) / set.length, q)));
			}
		}
		return result;
	}
}
