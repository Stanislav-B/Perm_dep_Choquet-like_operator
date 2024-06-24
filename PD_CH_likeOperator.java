import java.util.Arrays;
import java.util.concurrent.locks.Condition;

public class PD_CH_likeOperator {

	private double[] f;
	private int[] psi;
	private String[] Aname;
	private int[][] Aset;
	private double p;
	private double q;
	
	private double[] f_phi;
	private double[] Avalues;
	private double[] Avalues_psi;
	
	private double[] w={1.0/Math.sqrt(2),1,1.0/Math.sqrt(2),1,1,1.0/Math.sqrt(2),1,1.0/Math.sqrt(2)};
	
	private AuxiliaryMethods am = new AuxiliaryMethods();
	private ConditionalAggregationOperators cao = new ConditionalAggregationOperators();
	
	Measures measures= new Measures();
	
	// #1
	public PD_CH_likeOperator(double[] f, String[] Aname, int[][] Aset, int[] phi, int[] psi, double p, double q) {
		try {
			if (f.length!=Aname.length || f.length!=Aset.length) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("Incorrect input.");
			System.exit(1);
		}
		this.f=f;
		make_f_phi(phi);
		this.Aname=Aname;
		this.Aset=Aset;
		this.p=p;
		this.q=q;
		make_Avalues();
		make_Avalues_psi(psi);
	}
	
	// #2
	public PD_CH_likeOperator(double[] f, String[] Aname, int[][] Aset, String phi, int[] psi, double p, double q) {
		try {
			if (f.length!=Aname.length || f.length!=Aset.length) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("Incorrect input.");
			System.exit(1);
		}
		this.f=f;
		make_f_phi(phi);
		this.Aname=Aname;
		this.Aset=Aset;
		this.p=p;
		this.q=q;
		make_Avalues();
		make_Avalues_psi(psi);
	}
	
	// #3
	public PD_CH_likeOperator(double[] f, String[] Aname, int[][] Aset, int[] phi, String psi, double p, double q) {
		try {
			if (f.length!=Aname.length || f.length!=Aset.length) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("Incorrect input.");
			System.exit(1);
		}
		this.f=f;
		make_f_phi(phi);
		this.Aname=Aname;
		this.Aset=Aset;
		this.p=p;
		this.q=q;
		make_Avalues();
		make_Avalues_psi(psi);
	}
	
	// #4
	public PD_CH_likeOperator(double[] f, String[] Aname, int[][] Aset, String phi, String psi, double p, double q) {
		try {
			if (f.length!=Aname.length || f.length!=Aset.length) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("Incorrect input.");
			System.exit(1);
		}
		this.f=f;
		make_f_phi(phi);
		this.Aname=Aname;
		this.Aset=Aset;
		this.p=p;
		this.q=q;
		make_Avalues();
		make_Avalues_psi(psi);
	}
	
	
	
	
	
	
	private void make_f_phi(String phi){
		if (phi.equals("phiUp")) {
			f_phi=am.increasingArrange(f);
		}
		if (phi.equals("phiDown")) {
			f_phi=am.decreasingArrange(f);
		}
		if (phi.equals("phiId")) {
			f_phi=new double[f.length];
			for (int i = 0; i < f.length; i++) {
				f_phi[i]=f[i];
			}
		}
		if (phi.equals("phiIdW")) {
			f_phi=new double[f.length];
			for (int i = 0; i < f.length; i++) {
				f_phi[i]=f[i]*w[i];
			}
		}
	}
	
	
	private void make_f_phi(int[] phi){
		f_phi=new double[f.length];
		for (int i = 0; i < f.length; i++) {
			f_phi[i]=f[phi[i]];
		}
	}
	
	private void make_Avalues(){
		Avalues=new double[Aname.length];
		for (int i = 0; i < Aname.length; i++) {
			if (Aname[i].equals("sum")) {
				Avalues[i]=cao.sumAO(f_phi, Aset[i]);
			}
			if (Aname[i].equals("normSum")) {
				Avalues[i]=cao.normSumAO(f_phi, Aset[i]);
			}
			if (Aname[i].equals("pMean")) {
				Avalues[i]=cao.pMeanAO(f_phi, Aset[i],p);
			}
			if (Aname[i].equals("mean")) {
				Avalues[i]=cao.meanAO(f_phi, Aset[i]);
			}
			if (Aname[i].equals("max")) {
				Avalues[i]=cao.maximumAO(f_phi, Aset[i]);
			}
			if (Aname[i].equals("min")) {
				Avalues[i]=cao.minimumAO(f_phi, Aset[i]);
			}
			if (Aname[i].equals("maxMin")) {
				Avalues[i]=cao.maxMinAO(f_phi, Aset[i]);
			}
			if (Aname[i].equals("chiUniform")) {
				Avalues[i]=cao.ChoquetAOUniformMeasure(f_phi, Aset[i],q);
			}
			if (Aname[i].equals("minMax")) {
				Avalues[i]=cao.minMaxAO(f_phi, Aset[i]);
			}
		}
	}
		
	private void make_Avalues_psi(String psi) {
		if (psi.equals("psiUp")) {
			Avalues_psi = new double[Avalues.length];
			double[][] increasingPlusIndices=am.increasingArrangePlusIndices(Avalues);
			this.psi=new int[f.length];
			for (int i = 0; i < increasingPlusIndices.length; i++) {
				Avalues_psi[i]=increasingPlusIndices[i][0];
				this.psi[i]=(int) increasingPlusIndices[i][1];
			}
		}
		if (psi.equals("psiDown")) {
			Avalues_psi = new double[Avalues.length];
			double[][] degreasingPlusIndices=am.decreasingArrangePlusIndices(Avalues);
			this.psi=new int[f.length];
			for (int i = 0; i < degreasingPlusIndices.length; i++) {
				Avalues_psi[i]=degreasingPlusIndices[i][0];
				this.psi[i]=(int) degreasingPlusIndices[i][1];
			}
		}
	}
	
	private void make_Avalues_psi(int[] psi) {
		this.psi=psi;
		Avalues_psi = new double[Avalues.length];
		for (int i = 0; i < Avalues.length; i++) {
			Avalues_psi[i]=Avalues[psi[i]];
		}
	}
	
	public double getResult() {
		double result=0;
		for (int i = 0; i < f.length; i++) {
			int[] condSet=new int[f.length-i];
			for (int j = 0; j < condSet.length; j++) {
				condSet[j]=psi[i+j];
			}
			int[] condSetPlusOne=new int[f.length-i-1];
			for (int j = 0; j < condSetPlusOne.length; j++) {
				condSetPlusOne[j]=condSet[j+1];
			}
			result=result+(Avalues_psi[i]*(measures.getMeasure(condSet, w, q)-measures.getMeasure(condSetPlusOne, w, q)));
		}
		return result;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		double[] f= {10.0, 18.0, 16.0, 15.0, 9.0, 17.0, 13.0, 5.0};
		String[] Aname= {"normSum", "normSum", "normSum", "normSum", "chiUniform", "chiUniform", "chiUniform", "chiUniform"};
		int[][] Aset= {{0,1,2,3,4,5,6,7}, {3,4}, {1,2,5,6}, {0,7}, {0,1,2,3,4,5,6,7}, {0,1,6,7}, {1,2,5,6}, {0,1,2,5,6,7}};
		// "phiUp", "phiDown", "phiId", "phiIdW"
		// "psiUp", "psiDown"
		PD_CH_likeOperator cago=new PD_CH_likeOperator(f, Aname, Aset, "phiUp", "psiDown", 1, 0.9);
		System.out.println(cago.getResult());

	}

}
