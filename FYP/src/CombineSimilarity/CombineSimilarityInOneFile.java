/* This program combines the similarity of basic TF-IDF cosine similarity of both name 
 * and description of API into a single file for subsequent computation */
package CombineSimilarity;

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
	static String apiDescFilePath = "/API descriptions tf-idf results";
	static String apiNameFilePath = "/API Name tf-idf results";
	static String outputFilePath = "/API Name and Description results";
	public static void main(String[] args) throws IOException{
		File[] files = new File(apiDescFilePath).listFiles();
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
		FileInputStream fisDesc = new FileInputStream(apiDescFilePath + "\\" + fileName);
		BufferedReader brDesc = new BufferedReader(new InputStreamReader(fisDesc));
		
		FileInputStream fisName = new FileInputStream(apiNameFilePath + "\\" + fileName);
		BufferedReader brName = new BufferedReader(new InputStreamReader(fisName));
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + fileName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		ArrayList<String> nameLibrary = new ArrayList<String>();
		String nameLine = null;
		
		String[] nameLineArray = null;
		while ((nameLine = brName.readLine()) != null){
			nameLineArray = nameLine.split("###");
			if (nameLineArray.length == 3)
				nameLibrary.add(nameLineArray[0] + "###" + nameLineArray[2]);
			else
				nameLibrary.add(nameLine);
		}
		brName.close();
		
		HashMap<String, String> map = null;
		
		String descLine = null, API = "", combineResult = "";
		String[] descArray = new String[5];
		
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
					
					if (map != null && nameLibraryArray.length == 2){
						map.put(nameLibraryArray[0].trim(), nameLibraryArray[nameLibraryArray.length - 1]);
					}
					
					if (map != null && nameLibraryArray.length < 2){
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
				pwStream.println(descArray[0] + "###" + descArray[1] + "###" + combineResult);
			} else if (map != null && descArray.length < 3){
				map = null;
				pwStream.println();
			}
		}
		pwStream.close();
		brDesc.close();
	}
}
