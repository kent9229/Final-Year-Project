/* This program tokenizes the full-qualified API Names into individual elements */
package NameTFIDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TokenizeAPINames {
	static String apiDescFilePath = "/API descriptions";
	static String apiNameFilePath = "/API Name";
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
	        	tokenizeAPINames(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to tokenize the API Names
	 * @param fileName
	 * @throws IOException
	 */
	public static void tokenizeAPINames(String fileName) throws IOException{
		FileInputStream fis = new FileInputStream(apiDescFilePath + "\\" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter(apiNameFilePath + "\\" + fileName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null;
		String[] lineArray = new String[3];
		String[] APINameArray = new String[100];
		String extractedAPIName = "";
		String resultingSentence = "";
		
		while ((line = br.readLine()) != null){
			lineArray = line.split("###");
			APINameArray = lineArray[0].split("\\.");
			resultingSentence = "";
			if (APINameArray.length > 2){
				extractedAPIName = APINameArray[APINameArray.length - 2].trim();
				for (int i = 0; i < extractedAPIName.length(); i++){
					if (i + 1 != extractedAPIName.length() && Character.isUpperCase(extractedAPIName.charAt(i+1)))
						resultingSentence += extractedAPIName.charAt(i) + " ";
					else resultingSentence += extractedAPIName.charAt(i) + "";
				}
			}
			resultingSentence += " ";
			extractedAPIName = APINameArray[APINameArray.length - 1].trim();
			for (int i = 0; i < extractedAPIName.length(); i++){
				if (i + 1 != extractedAPIName.length() && Character.isUpperCase(extractedAPIName.charAt(i+1)))
					resultingSentence += extractedAPIName.charAt(i) + " ";
				else resultingSentence += extractedAPIName.charAt(i) + "";
			}
			
			pwStream.println(lineArray[0] + "###" + resultingSentence);
		}
		pwStream.close();
		br.close();
	}
}
