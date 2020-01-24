package Matrixmean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Matrixmean {
	private static int MSIZE;


	private static String fileName="File.txt";
	private static String fileNameS="FileS.txt";
	private static String nullfile;
        private static double [][] A;
	
	
	private static void usage() {
		System.out.println("    Usage: Matrixmean.Matrixmean <MSize> ");
	}
	
	public static void main(String[] args) throws Exception {
		// Check and get parameters
		if (args.length != 1) {
			usage();
			throw new Exception("[ERROR] Incorrect number of parameters");
		}
		MSIZE = Integer.parseInt(args[0]);
		
		
		// Initialize matrices
		System.out.println("[LOG] MSIZE parameter value = " + MSIZE);
		A = initializeMatrix();
		
		//printMatrix(A, "InitialMatrix");
		
		File f = new File(fileName);
		f.createNewFile();
		File f2 = new File(fileNameS);
		f2.createNewFile();
		Double[] mean = new Double[MSIZE];
		for (int i =0; i<MSIZE; i++) {
			mean[i] = MatrixmeanImpl.computeMean(A[i], nullfile);
		}
		
		for (int i =0; i<MSIZE; i++) {
			MatrixmeanImpl.computeSquare(fileNameS, mean[i]);
		}
		Double total = new Double(0);
		
		for (int i=0; i<MSIZE;i++) {
			total = total + mean[i];
		}
	}
	
	private static double[][] initializeMatrix() {
		double[][] matrix = new double[MSIZE][MSIZE];
		for (int i = 0; i < MSIZE; ++i) {
			
				matrix[i] = MatrixmeanImpl.initializeRaw(MSIZE);
			
		}
		
		return matrix;
	}
	
	@SuppressWarnings("unused")
	private static void storeValues(String fileName) throws IOException {
	
			FileInputStream fis = new FileInputStream(fileName);
			File textfile = new File("./final.txt");
			FileOutputStream fos = new FileOutputStream(textfile);
			byte [] buffer = new byte [8*1024];
			int bytesRead;
			while((bytesRead = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
			fis.close();
			fos.close();
    	
	}
	
	@SuppressWarnings("unused")
	private static void printMatrix(double[][] matrix, String name) {
		System.out.println("MATRIX " + name);
		for (int i = 0; i < MSIZE; i++) {
			 for (int j = 0; j < MSIZE; j++) {
				 System.out.print((matrix[i][j]) + " ");
			 }
			 System.out.println("");
		 }
	}

}
