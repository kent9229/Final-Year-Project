/* This program processes the input query API before feeding into Lucene */
package ProcessPackageAndAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PrepareQueryAPI {
	static String filePath = "";
	public static void main(String[] args) throws IOException{
		File[] files = new File(filePath + "API descriptions").listFiles();
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
	        	prepareQueryAPI(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to process the query information
	 * @param fileName
	 * @throws IOException
	 */
	public static void prepareQueryAPI(String fileName) throws IOException {
		FileInputStream fisAPI = new FileInputStream(filePath + "API descriptions\\" + fileName);
		BufferedReader brAPI = new BufferedReader(new InputStreamReader(fisAPI));
		
		FileWriter fwStream = new FileWriter(filePath + "QueryAPI\\" + fileName.split("\\.csv")[0] + ".txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null;
		String[] lineArray = null;
		String[] descArray = null;
		String finalDesc = "";
		
		while ((line = brAPI.readLine()) != null){
			lineArray = line.split("###");
			descArray = lineArray[1].split("\\. ");
			if (descArray.length > 1) finalDesc = lineArray[0] + "###" + descArray[0].replaceAll("\\.", "") + " " + descArray[1].replaceAll("\\.", "");
			else if (!lineArray[1].trim().equals("") && !lineArray[1].trim().equals("\u00A0")) finalDesc = lineArray[0] + "###" + lineArray[1].replaceAll("\\.", "");
			pwStream.println(finalDesc);
		}
		pwStream.close();
		brAPI.close();
		
	}
}
