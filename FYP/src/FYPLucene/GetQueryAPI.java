/* This program consolidate all the source APIs to prepare to subsequent queries */
package FYPLucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GetQueryAPI {
	static String inputFilePath = "/Evaluation";
	static String outputFilePath = "/QueryAPI";
	public static void main(String[] args) throws IOException {
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
	        	getQueryAPI(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to consolidate all the source APIs
	 * @param fileName
	 * @throws IOException
	 */
	public static void getQueryAPI(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		ArrayList<String> AL = new ArrayList<String>();
		
		while ((line = br.readLine()) != null){
			String[] lineArray = line.split(" vs ");
			if (!AL.contains(lineArray[0]))
				AL.add(lineArray[0]);
		}
		
		String targetFileName = fileName.split(" vs ")[0].trim();
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + targetFileName + ".txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		for (String eachAPI: AL)
			pwStream.println(eachAPI + "###" + eachAPI.replaceAll("\\.", " "));
		pwStream.close();
		br.close();
	}
}
