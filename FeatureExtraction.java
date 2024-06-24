import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FeatureExtraction {

	private BufferedImage image;
	private int sigma; // the radius of the area of aggregated pixels
	private double q; // parameter (power) of uniform measure
	private double p; // parameter for p-Mean, p>0
	
	private int width; // width of input image
	private int height; // height of input image

	private int[][] resultImage; // array of aggregated pixels
	private int[] set; // auxiliary set for calculations
	
	//Measures measures = new Measures();
	AuxiliaryMethods am= new AuxiliaryMethods();
	
	//private double[] measure;
	private String nameOfOutputFile;
	
	private String[] Aname; 
	private int[][] Aset;
	private String phi;
	private String psi;
	
	public FeatureExtraction(int sigma, String[] Aname, int[][] Aset, String phi, String psi, double p, double q, String inputFile) {
		try {
			if (sigma<=0 || sigma>3) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("Sigma must be >0 a <=3.");
			System.exit(1);
		}
		try {
			image = ImageIO.read(new File(inputFile));
			this.sigma=sigma;
			this.q=q;
			this.p=p;
			this.phi=phi;
			this.psi=psi;
			this.Aname=Aname;
			this.Aset=Aset;
			width = image.getWidth();
			height = image.getHeight();
			resultImage = new int[width][height];
			//set=new int[(int) Math.pow(2*sigma+1, 2)-1];
			//for (int i = 0; i < set.length; i++) {
				//set[i]=i;
			//}
			//measure=measures.uniformMeasureOnPowerset(set.length, q);
		} catch (IOException e) {
			System.err.println("The file " + inputFile + " failed to load.");
			System.exit(1);
		}
		nameOfOutputFile=am.nameOfInputFileWithoutSuffix(inputFile);
		doWork();
		makeImage();
	}
	
	private void doWork() {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				resultImage[i][j]=aggregatePixelDifferences(neighborAndCenterPixelDifferences(i, j));
			}
		}
	}
	
	private int aggregatePixelDifferences(double[] values) {
		PD_CH_likeOperator cago = new PD_CH_likeOperator(values, Aname, Aset, phi, psi, p, q);
		return (int) cago.getResult();
	}
	
	private double[] neighborAndCenterPixelDifferences(int x, int y) {
		double[] result=new double[(int) Math.pow(2*sigma+1, 2)-1];
		int positionX=x-sigma;
		int positionY=y-sigma;
		int counter=0;
		Color colorCenter=new Color(image.getRGB(x, y)); //image is greyscal
		double greyCenter=colorCenter.getRed(); // R=G=B
		Color colorNeighbor; 
		double greyNeighbor;
		//we adjust the coordinates for the image edges
		int forX;
		int forY;
		for (int i = 0; i < 2*sigma+1; i++) {
			for (int j = 0; j < 2*sigma+1; j++) {
				if (positionX+j!=x && positionY+i!=y) {
					forX=positionX+j;
					forY=positionY+i;
					if (forX<0) {
						forX=Math.abs(forX);
					}
					if (forY<0) {
						forY=Math.abs(forY);
					}
					if (forX>=width) {
						forX=x-(forX-x);
					}
					if (forY>=height) {
						forY=y-(forY-y);
					}
					colorNeighbor= new Color(image.getRGB(forX, forY));
					greyNeighbor=colorNeighbor.getRed();
					result[counter]=Math.abs(greyCenter-greyNeighbor);
					counter++;
				}
				
			}
		}
		return result;
	}
	
	
	private void makeImage() {
		BufferedImage outputImage = new BufferedImage(width, height, image.getType());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				outputImage.setRGB(i, j, new Color(resultImage[i][j], resultImage[i][j], resultImage[i][j]).getRGB());
			}
		}
		try {
			ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_CAG.png"));
		} catch (Exception e) {
			System.out.println("Exception occured :" + e.getMessage());
		}
		System.out.println("Image was written succesfully.");
	}
	
	
	public static void main(String[] args) {
		String[] Aname= {"normSum", "normSum", "normSum", "normSum", "chiUniform", "chiUniform", "chiUniform", "chiUniform"};
		int[][] Aset= {{0,1,2,3,4,5,6,7}, {3,4}, {1,2,5,6}, {0,7}, {0,1,2,3,4,5,6,7}, {0,1,6,7}, {1,2,5,6}, {0,1,2,5,6,7}};
		int[][] Aset2= {{0,1,2,3,4,5,6,7}, {1,3,4,6}, {0,2,5,7}, {3,4}, {0,1,2,3,4,5,6,7}, {0,2,5,7}, {1,3,4,6}, {1,6}};
		
		FeatureExtraction fe = new FeatureExtraction(
		1, // int sigma - polomer od stredoveho pixelu
		Aname,
		Aset2,
		"phiUp",
		"psiDown",
		1.0, // double p - parameter pre p-priemer, p>0
		0.9, // double q - mocnina pre uniformnu mieru
		"C:\\Users\\Stanislav\\Desktop\\detection_figures\\4. S2 figures_greyscale_gauss(s=2)\\223060_grey_gauss(s=2).png" 
		);
	}
	
	
}
