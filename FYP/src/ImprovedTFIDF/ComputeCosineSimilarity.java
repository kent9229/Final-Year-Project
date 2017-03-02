/* Compute cosine similarity between packages or class of similar libraries */
package ImprovedTFIDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ComputeCosineSimilarity {
	static ArrayList<String> similarLibraries = new ArrayList<String>();
	static String inputFilePath = "/API Name and Descriptions vectors (Improved TFIDF)";
	static String outputFilePath = "/API Name and Descriptions results";
	static String simLibFilePath = "/javaSimilarLib_ranked.txt";
	
	public static void main(String[] args) throws IOException{
		
		File[] files = new File(inputFilePath).listFiles();
		readInSimilarLibraries();
		for (String similarLib: similarLibraries){
			computeCSBetweenLib(files, similarLib);
		}
	}
	
	/**
	 * Read the similar libraries into an array list
	 * @throws IOException
	 */
	public static void readInSimilarLibraries() throws IOException{
		FileInputStream fis = new FileInputStream(simLibFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		String[] lineArray = new String[2];
		while ((line = br.readLine()) != null){
			similarLibraries.add(line.replace("\t", "###"));
		}
	}
	
	/**
	 * To loop through a directory to process all files
	 * @param files
	 * @throws IOException
	 */
	public static void computeCSBetweenLib(File[] files, String similarLib) throws IOException {
	   for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            computeCSBetweenLib(file.listFiles(), similarLib);
	        } else {
	        	if (file.getName().split("\\.csv")[0].trim().toLowerCase().equals(similarLib.split("###")[0])){
	        		readFile(similarLib);
	        	}
	        }
	    }
	}
	
	/**
	 * Read each file and retrieve its respective vector coordinates
	 * @param fin
	 * @throws IOException
	 	*/
	private static void readFile(String similarLib) throws IOException {
		FileInputStream baseLibFis = new FileInputStream(inputFilePath + "\\" + similarLib.split("###")[0] + ".csv");
		BufferedReader baseLibBr = new BufferedReader(new InputStreamReader(baseLibFis));
		
		FileInputStream targetLibFis = new FileInputStream(inputFilePath + "\\" + similarLib.split("###")[1] + ".csv");
		BufferedReader targetLibBr = new BufferedReader(new InputStreamReader(targetLibFis));
		
		ArrayList<String> targetLibrary = new ArrayList<String>();
		String targetLine = null;
		
		while ((targetLine = targetLibBr.readLine()) != null)
			targetLibrary.add(targetLine);
		targetLibBr.close();
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + similarLib.split("###")[0] + " vs " + similarLib.split("###")[1] + ".csv", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String[] baseLineArray = new String[300];
		ArrayList<String> baseWordArray = new ArrayList<String>();
		ArrayList<Double> basePackageVector = new ArrayList<Double>();
		
		String[] targetLineArray = new String[300];
		ArrayList<String> targetWordArray = new ArrayList<String>();
		ArrayList<Double> targetPackageVector = new ArrayList<Double>();
		double cosineSimilarityScore = 0;
		
		String line = null;
		
		while ((line = baseLibBr.readLine()) != null){
			baseLineArray = line.split("###");
			Collections.addAll(baseWordArray, baseLineArray[baseLineArray.length - 1].trim().split(" "));
			
			pwStream.println(baseLineArray[0] + "###" + baseLineArray[1] + "###" + similarLib.split("###")[0] + "###" + similarLib.split("###")[1]);
			
			for (String targetLib: targetLibrary){
				targetLineArray = targetLib.split("###");
				Collections.addAll(targetWordArray, targetLineArray[targetLineArray.length - 1].trim().split(" "));
				
				cosineSimilarityScore = cosineSimilarity(baseWordArray, targetWordArray);
				
				pwStream.println(targetLineArray[0] + "###" + targetLineArray[1] + "###" + cosineSimilarityScore);
				targetPackageVector = new ArrayList<Double>();
				targetWordArray = new ArrayList<String>();
			}
			basePackageVector = new ArrayList<Double>();
			baseWordArray = new ArrayList<String>();
			
			pwStream.println();
		}
		pwStream.close();
		baseLibBr.close();
	}
	
	/**
	 * Compute the cosine similarity between 2 vectors
	 * @param vectorA
	 * @param vectorB
	 * @return computed cosine similarity
	 */
	public static double cosineSimilarity(ArrayList<String> vectorA, ArrayList<String> vectorB) {
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    
	    for (int i = 0; i < vectorA.size(); i++) {
	    	normA += Math.pow(Double.parseDouble(vectorA.get(i).split(",")[1].trim()), 2);
	    }
	    
	    for (int j = 0; j < vectorB.size(); j++) {
	    	normB += Math.pow(Double.parseDouble(vectorB.get(j).split(",")[1].trim()), 2);
	    }
	    
	    for (int i = 0; i < vectorA.size(); i++) {
	    	for (int j = 0; j < vectorB.size(); j++) {
	    		if (vectorA.get(i).split(",")[0].trim().equals(vectorB.get(j).split(",")[0].trim())){
	    			dotProduct += Double.parseDouble(vectorA.get(i).split(",")[1].trim()) * Double.parseDouble(vectorB.get(j).split(",")[1].trim());
	    			break;
	    		}
	    	}
	    }   
	    if (dotProduct != 0.0)
	    	return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	    else return 0.0;
	}
}
