/*
 * This program performs basic computation of vectors (used for Method 2)
 */
package Method2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VectorComputation {
	static ArrayList<String> similarLibraries = new ArrayList<String>();
	static String inputFilePath = "/Method 2\\Packages";
	static String outputFilePath = "/Method 2\\Vectors after subtraction";
	public static void main(String[] args) throws IOException{
		File[] files = new File(inputFilePath).listFiles();
		showFiles(files);

	}
	
	/**
	 * To loop through a directory to process all files
	 * @param files
	 * @throws IOException
	 */
	public static void showFiles(File[] files) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles());
            } else {
            	vectorComputation(file.getName());
            }
        }
    }
	
	/**
	 * Compute the vector difference between the packages and their respective libraries
	 * @param libraryName
	 * @throws IOException
	 */
	public static void vectorComputation(String libraryName) throws IOException{
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + libraryName);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + libraryName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null;
		String[] lineArray;
		double[] classVector = new double[200];
		double[] resultVector = new double[200];
		
		while ((line = br.readLine()) != null){
			lineArray = line.split(" ");
			String result = lineArray[0] + " ";
			for (int i = 1; i < lineArray.length; i++){
				classVector[i-1] = Double.parseDouble(lineArray[i].trim());
			}
			resultVector = subtractionVector(classVector, readLibraryVector(libraryName.split("\\.csv")[0].trim()));
			for (int j = 0; j < resultVector.length; j++){
				result += resultVector[j] + " ";
			}
			pwStream.println(result);
		}
		pwStream.close();
	}
	
	/**
	 * Read in library vector
	 * @param libraryName
	 * @return
	 * @throws IOException
	 */
	public static double[] readLibraryVector(String libraryName) throws IOException{
		FileInputStream fis = new FileInputStream(inputFilePath + "\\Library Vectors.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		String[] lineArray = new String[201];
		double[] libraryVector = new double[200];
		
		int found = 0;
		while ((line = br.readLine()) != null){
			lineArray = line.split(" ");
			
			if (lineArray[0].toLowerCase().trim().equals(libraryName)){
				for (int i = 1; i < lineArray.length; i++){
					libraryVector[i-1] = Double.parseDouble(lineArray[i]);
				}
				found = 1;
				break;
			}
		}
		return libraryVector;
	}
	
	/**
	 * Adding 2 vectors together
	 * @param vector1
	 * @param vector2
	 * @return added vector
	 */
	public static double[] additionVector(double[] vector1, double[] vector2){
		double[] resultVector = new double[200];
		
		for (int i = 0; i < vector1.length; i++){
			resultVector[i] = vector1[i] + vector2[i];
		}
		return resultVector;
	}
	
	/**
	 * Subtracting vector2 from vector1
	 * @param vector1
	 * @param vector2
	 * @return subtracted vector
	 */
	public static double[] subtractionVector(double[] vector1, double[] vector2){
		double[] resultVector = new double[200];	
		
		for (int i = 0; i < vector1.length; i++){
			resultVector[i] = vector1[i] - vector2[i];
		}
		
		return resultVector;
	}
}
