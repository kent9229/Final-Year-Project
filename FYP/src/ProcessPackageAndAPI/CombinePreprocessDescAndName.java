/* This class is combine the preprocessed name and description of each package and API */
package ProcessPackageAndAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CombinePreprocessDescAndName {
	static String inputFilePath = "/API descriptions (preprocessed)";
	static String outputFilePath = "/API Name and Descriptions (preprocessed)";
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
	        	CombinePreprocessDescAndName(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to combine preprocessed package/API Name and description
	 * @param fileName
	 * @throws IOException
	 */
	public static void CombinePreprocessDescAndName(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream("C:\\Users\\Kent Ong\\Dropbox\\CSC\\FYP\\Data\\Crawler\\TFIDF (Preprocessed APIDesc and extracted APIName)\\API descriptions (preprocessed)\\" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter("C:\\Users\\Kent Ong\\Dropbox\\CSC\\FYP\\Data\\Crawler\\TFIDF (Preprocessed APIDesc and extracted APIName)\\API Name and Descriptions (preprocessed)\\" + fileName, true);
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
				resultingSentence = CombineClassAndAPI.splitCamelCase(APINameArray[APINameArray.length - 2].trim());
			}
			resultingSentence += " ";
			resultingSentence += CombineClassAndAPI.splitCamelCase(APINameArray[APINameArray.length - 1].trim());
			
			if (lineArray.length > 1)
				pwStream.println(lineArray[0] + "###" + lineArray[1].trim() + " " + resultingSentence.toLowerCase());
			else
				pwStream.println(lineArray[0] + "###" + resultingSentence.toLowerCase());
		}
		pwStream.close();
		br.close();
	}

	
}
