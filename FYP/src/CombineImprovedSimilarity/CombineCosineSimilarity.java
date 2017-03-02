/* This program combines the modified TF-IDF cosine similarity of both name 
 * and description of API with weight a and b respectively */
package CombineImprovedSimilarity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CombineCosineSimilarity {
	static String inputFilePath = "/API Name and Description results (Improved)";
	static String outputFilePath = "/API Name and Description results (Improved weighted)"; 
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
	        	combineSimilarity(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method combine cosine similarity of name and description of API
	 * @param fileName
	 * @throws IOException
	 */
	public static void combineSimilarity(String fileName) throws IOException{
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		String[] lineArray = new String[5];
		double nameSimilarity = 0.0, descSimilarity = 0.0;
		double a = 0.0, b, combineSimilarity = 0.0;
		
		while ((line = br.readLine()) != null){
			lineArray = line.split("###");
			a = 0.7;
			b = 1 - a;
			
			FileWriter fwStream = new FileWriter(outputFilePath + "\\" + fileName, true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
			if (lineArray.length != 3){
				pwStream.println(line);
			} else if (lineArray.length == 3){
				nameSimilarity = Double.parseDouble(lineArray[1]);
				descSimilarity = Double.parseDouble(lineArray[2]);
				combineSimilarity = a * nameSimilarity + b * descSimilarity;
				pwStream.println(lineArray[0] + "###" + round(Double.parseDouble(lineArray[1]), 2) + "###" + round(Double.parseDouble(lineArray[2]), 2) + "###" +  + round(combineSimilarity, 2));
			}
			pwStream.close();
			
		}
		br.close();
	}
	/**
	 * To round a value to the specified number of decimal places
	 * @param value
	 * @param places
	 * @return rounded value
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
