/* This program computes the recall rate for the libraries involved in the ground truth 
 * for basic TF-IDF approach for API Name */
package NameTFIDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Recall {
	static ArrayList<String> overallAL = new ArrayList<String>();
	static String inputFilePath = "/API Name tf-idf results (sorted)\\MRR";
	static String outputFilePath = "/API Name tf-idf results (sorted)\\Recall";
	public static void main(String[] args) throws IOException{
		int[] recallValues = {1,5,10};
		File[] files = new File(inputFilePath).listFiles();
        for (int i = 0; i < recallValues.length; i++){
        	showFiles(files, recallValues[i]);
        	averageRecall(recallValues[i]);
        }
	}
	
	/**
	 * To loop through a directory to process all files
	 * @param files
	 * @param recallValue
	 * @throws IOException
	 */
	public static void showFiles(File[] files, int recallValue) throws IOException {
        overallAL = new ArrayList<String>();
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles(), recallValue);
            } else {
                subsetRecall(file.getName(), recallValue);
            }
        }
    }
	
	/**
	 * Computes the recall rate for each pair of similar libraries
	 * @param fileName
	 * @param recallValue
	 * @throws IOException
	 */
	public static void subsetRecall(String fileName, int recallValue) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(inputFilePath + "\\" + fileName));
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\Recall@" + recallValue + ".txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null;
		String[] lineArray = new String[3];
		
		int recallResult = 0;
		while ((line = br.readLine()) != null){
			recallResult = 0;
			lineArray = line.split("\t");
			if (!lineArray[2].equals("null")){
				if (Integer.parseInt(lineArray[2]) <= recallValue){
					recallResult = 1;
				}
				pwStream.println(lineArray[0] + "\t" + lineArray[1] + "\t" + recallResult);
				overallAL.add(lineArray[0] + "\t" + lineArray[1] + "\t" + recallResult);
			}
		}
		pwStream.close();
		br.close();
	}
	
	/**
	 * Compute the average recall rate for all similar library pairs
	 * @param recallValue
	 * @throws IOException
	 */
	public static void averageRecall(int recallValue) throws IOException{
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\Recall@" + recallValue + ".txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
        
        double total = 0;
        
        for (int i = 0; i < overallAL.size(); i++)
        	total += Integer.parseInt(overallAL.get(i).split("\t")[2]);
        
        pwStream.println("Average Recall\t" + total/overallAL.size());
        pwStream.close();
	}
}
