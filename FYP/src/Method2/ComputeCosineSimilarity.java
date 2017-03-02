/* This program computes cosine similarity of vectors (used for Method 2) */
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

public class ComputeCosineSimilarity {
	static ArrayList<String> similarLibraries = new ArrayList<String>();
	static String inputFilePath = "/Method 2\\Vectors after subtraction";
	static String outputFilePath = "/Method 2\\Results";
	static String simLibFilePath = "/javaSimilarLib_ranked.txt";
	public static void main(String[] args) throws IOException{
		readInSimilarLibraries();
		for (String similarLib: similarLibraries){
			cosineSimilarityComputation(similarLib.split("###")[0].trim(), similarLib.split("###")[1].trim());
		}
	}
	
	/**
	 * Read similar libraries into array list
	 * @throws IOException
	 */
	public static void readInSimilarLibraries() throws IOException{
		FileInputStream fis = new FileInputStream(simLibFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		
		while ((line = br.readLine()) != null){
			similarLibraries.add(line.replace("\t", "###"));
		}
	}
	
	/**
	 * Compute cosine similarity between 2 libraries
	 * @param libraryName
	 * @param targetLibrary
	 * @throws IOException
	 */
	private static void cosineSimilarityComputation(String libraryName, String targetLibrary) throws IOException {
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + libraryName + ".csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileInputStream fis1 = new FileInputStream(inputFilePath + "\\" + targetLibrary + ".csv");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + libraryName.trim() + " vs " + targetLibrary + ".csv", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String[] lineArray = new String[201];
		String[] lineArray1 = new String[201];
		double[] classVector = new double[200];
		double[] targetLibraryClassVector = new double[200]; 
		double cosineSimilarity = 0;
		
		String line = null, line1 = null;
		
		HashMap<String, double[]> map = new HashMap<String, double[]>();
		while ((line1 = br1.readLine()) != null){
			lineArray1 = line1.split(" ");
			for (int i = 1; i < lineArray1.length; i++){
				targetLibraryClassVector[i-1] = Double.parseDouble(lineArray1[i]);
			}
			map.put(lineArray1[0], targetLibraryClassVector);
			targetLibraryClassVector = new double[200];
		}
		br1.close();
		while ((line = br.readLine()) != null) {
			lineArray = line.split(" ");
			pwStream.println(lineArray[0] + "###" + libraryName + "###" + targetLibrary);
			for (int i = 1; i < lineArray.length; i++){
				classVector[i-1] = Double.parseDouble(lineArray[i]);
			}
			
			for(Map.Entry<String, double[]> entry: map.entrySet()){
				cosineSimilarity = cosineSimilarity(classVector, entry.getValue());
				pwStream.println(entry.getKey() + "###" + cosineSimilarity);
			}
			
			pwStream.println();
		}
		pwStream.close();
		br.close();
		
	}
	
	/**
	 * Compute cosine similarity between 2 vectors
	 * @param vectorA
	 * @param vectorB
	 * @return computed cosine similarity
	 */
	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
	    double dotProduct = 0.0, normA = 0.0, normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
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
}
