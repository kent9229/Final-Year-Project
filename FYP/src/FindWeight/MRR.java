/* This program computes the Mean Reciprocal Rank for the libraries 
 * involved in the ground truth for 21 iterations */
package FindWeight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MRR {
	static ArrayList<String> overallAL = new ArrayList<String>();
	static String filePath = "/API Name and Description results (sorted)";
	public static void main(String[] args) throws IOException{
		double a = 0.0;
		while (CombineCosineSimilarity.round(a,2) <= 1.0){
			File[] files = new File(filePath + "\\" + CombineCosineSimilarity.round(a, 2) + "\\MRR").listFiles();
	        showFiles(files, CombineCosineSimilarity.round(a, 2));
	        a += 0.05;
		}
	}
	
	/**
	 * To loop through a directory to process all files
	 * @param files
	 * @param weight
	 * @throws IOException
	 */
	public static void showFiles(File[] files, double weight) throws IOException {
        overallAL = new ArrayList<String>();
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles(), weight);
            } else {
                subsetMRR(file.getName(), weight);
            }
        }
        overallMRR(weight);
    }
	
	/**
	 * Compute MRR for each pair of similar libraries
	 * @param fileName
	 * @param weight
	 * @throws IOException
	 */
	public static void subsetMRR(String fileName, double weight) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filePath + "\\" + weight + "\\MRR\\" + fileName));
		
		FileWriter fwStream = new FileWriter(filePath + "\\" + weight + "\\MRR.txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null;
		String[] lineArray = new String[3];
		double total = 0.0;
		int count = 0;
		while ((line = br.readLine()) != null){
			lineArray = line.split("\t");
			if (!lineArray[2].equals("null")){
				total += 1 / Double.parseDouble(lineArray[2]);
				count++;
				overallAL.add(line);
			}
		}
		
		pwStream.println(fileName.split(" \\(FindRanks\\)")[0].replace(".txt", "") + "\t" + total/count);
		pwStream.close();
		br.close();
	}
	
	/**
	 * Compute overall MRR for all APIs
	 * @param weight
	 * @throws IOException
	 */
	public static void overallMRR(double weight) throws IOException{
		FileWriter fwStream = new FileWriter(filePath + "\\" + weight + "\\MRR.txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
        
        String[] lineArray = new String[3];
        double total = 0.0;
        for (int i = 0; i < overallAL.size(); i++){
        	lineArray = overallAL.get(i).split("\t");
        	total += 1 / Double.parseDouble(lineArray[2]);
        }
        pwStream.println("Overall MRR \t" + total/overallAL.size());
        pwStream.close();
	}

}
