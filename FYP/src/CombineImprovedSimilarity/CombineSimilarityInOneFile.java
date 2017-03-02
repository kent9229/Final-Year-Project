/* This program combines the similarity of modified TF-IDF cosine similarity of both 
 * name and description of API into a single file for subsequent computation */
package CombineImprovedSimilarity;

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

public class CombineSimilarityInOneFile {
	static String apiDescInputFilePath = "/API descriptions tf-idf results (Improved)";
	static String apiNameInputFilePath = "/API Name tf-idf results (Improved)"; 
	static String outputFilePath = "/API Name and Description results (Improved)";
	public static void main(String[] args) throws IOException{
		File[] files = new File(apiDescInputFilePath).listFiles();
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
	 * Main method to combine the cosine similarity of name and description into one file
	 * @param fileName
	 * @throws IOException
	 */
	public static void combineSimilarity(String fileName) throws IOException{
		FileInputStream fisDesc = new FileInputStream(apiDescInputFilePath + "\\" + fileName);
		BufferedReader brDesc = new BufferedReader(new InputStreamReader(fisDesc));
		
		FileInputStream fisName = new FileInputStream(apiNameInputFilePath + "\\" + fileName);
		BufferedReader brName = new BufferedReader(new InputStreamReader(fisName));
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + fileName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		ArrayList<String> nameLibrary = new ArrayList<String>();
		String nameLine = null;
		
		while ((nameLine = brName.readLine()) != null){
			nameLibrary.add(nameLine);
		}
		brName.close();
		
		HashMap<String, String> map = null;
		
		String descLine = null;
		String[] descArray = new String[5];
		String API = "";
		String combineResult = "";
		
		while ((descLine = brDesc.readLine()) != null){
			descArray = descLine.split("###");
			if (map == null && descArray.length == 4){
				for (int i = 0; i < nameLibrary.size(); i++){
					String[] nameLibraryArray = new String[5];
					nameLibraryArray = nameLibrary.get(i).split("###");
					
					if (nameLibraryArray.length == 4 && descArray[0].equals(nameLibraryArray[0])){
						pwStream.println(descLine);
						map = new HashMap<String, String>();
					}
					
					if (map != null && nameLibraryArray.length == 3){
						map.put(nameLibraryArray[0].trim(), nameLibraryArray[nameLibraryArray.length - 1]);
					}
					
					if (map != null && nameLibraryArray.length < 3){
						while (i >=0){
							nameLibrary.remove(i);
							i--;
						}
						break;
					}
				}
			} else if (map != null && descArray.length == 3){
				// NameSimilarity###DescSimilarity
				combineResult = map.get(descArray[0].trim()) + "###";
				combineResult += descArray[descArray.length - 1];
				pwStream.println(descArray[0] + "###" + combineResult);
			} else if (map != null && descArray.length < 3){
				map = null;
				pwStream.println();
			}
		}
		pwStream.close();
		brDesc.close();
	}
}
