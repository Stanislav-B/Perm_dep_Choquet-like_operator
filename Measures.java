import java.util.Arrays;

public class Measures {
	
	private AuxiliaryMethods am= new AuxiliaryMethods();

	// \mu(E)=(\frac{|X|}{n})^q
	// produces for each set from the powerset its monotone measure
	
	public double[] uniformMeasureOnPowerset(int n, double q) {
		double[] result=new double[(int) Math.pow(2,n)];
		int count=1;
		int numberOfElements=0;
		for (int i = 1; i <= n; i++) {
			numberOfElements=am.factorial(n)/(am.factorial(n-i)*am.factorial(i));
			for (int j = count; j < count+numberOfElements; j++) {
				result[j]= Math.pow(((double) i/ n), q) ;
			}
		count=count+numberOfElements;	
		}
		return result;
	}
	
	public double getMeasure(int[] set, double[] weights, double q) {
		if (set.length==0) {
			return 0;
		}
		double result=0;
		double sumWeights=0;
		for (int i = 0; i < weights.length; i++) {
			sumWeights=sumWeights+weights[i];
		}
		double k=(weights.length)/(sumWeights);
		double sumWeightsB=0;
		for (int i = 0; i < set.length; i++) {
			sumWeightsB=sumWeightsB+weights[set[i]];
		}
		sumWeightsB=sumWeightsB/set.length;
		result=Math.pow((double) set.length/weights.length, q)*sumWeightsB*k;
		return result;
	}
	
	
	public static void main(String[] args) {
		int[][] Aset= {{0,1,2,3,4,5,6,7}, {3,4}, {1,2,5,6}, {0,7}, {0,1,2,3,4,5,6,7}, {0,1,6,7}, {1,2,5,6}, {0,1,2,5,6,7}};
		double[] weights= {1.0/Math.sqrt(2),1,1.0/Math.sqrt(2),1,1,1.0/Math.sqrt(2),1,1.0/Math.sqrt(2)};
		
		Measures m = new Measures();
		
		System.out.println(m.getMeasure(
		Aset[3], // int sigma - polomer od stredoveho pixelu
		weights, // double q - mocnina pre uniformnu mieru
		0.9 // double p - parameter pre p-priemer, p>0
		));
	}
}
