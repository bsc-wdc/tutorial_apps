package Matrixmean;


import java.io.FileOutputStream;
import java.io.IOException;

public class MatrixmeanImpl {
	public static double[] initializeRaw(int size) {
		double[] raw = new double[size];
		for (int i = 0; i < size; ++i) {
			raw[i] = (double)(Math.random()*10.0);
		}
		
		return raw;
	}
	
	public static Double computeMean(double[] a, String fileName) throws IOException {
		double acum=0;
		for (int i = 0; i < a.length; i++) {
			acum=acum+a[i];
		}
		
		acum=acum/(double)a.length;
		FileOutputStream fos = new FileOutputStream(fileName,true);
		String info = String.valueOf(acum);
		info += "\n";
		fos.write(info.getBytes());
		fos.close();
	 	return null;	
		//return acum;
	}
	public static void computeSquare(String fileName, Double mean) throws IOException {
		double acum=0;
		
		
		acum=Math.sqrt(mean);
		FileOutputStream fos = new FileOutputStream(fileName,true);
		String info = String.valueOf(acum);
		info += "\n";
		fos.write(info.getBytes());
		fos.close();
	}
}
