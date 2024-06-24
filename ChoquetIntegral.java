import java.util.Arrays;

public class ChoquetIntegral {

	private AuxiliaryMethods am = new AuxiliaryMethods();

	// measure always in shape:
	// {emptyset, {1},{2}, ...,{n}, {1,2},{1,3},...,{1,n}, {2,3},...,{2,n},
	// ...,{1,2,...,n} }

	public double CHI(double[] vector, double[] measure) {
		double[][] increasingVectorAndIndices = am.increasingArrangeAndAddIndices(vector);
		double integralResult = increasingVectorAndIndices[0][0] * measure[measure.length - 1];
		for (int i = 1; i < increasingVectorAndIndices.length; i++) {
			integralResult = integralResult + ((increasingVectorAndIndices[i][0] - increasingVectorAndIndices[i - 1][0])
					* am.findMeasure(increasingVectorAndIndices, measure, i));
		}
		return integralResult;
	}

	public static void main(String[] args) {
		ChoquetIntegral chi = new ChoquetIntegral();
		double[] vector = { 0.2, 0.9, 0.9, 0.8 };
		double[] measure = { 0, 0.15, 0.18, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 1 };

		System.out.println("CHI=" + chi.CHI(vector, measure));
	}
}
