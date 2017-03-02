/* This class is combine the name and description of each API */

package CombineAPINameDesc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CombineAPINameDesc {
	static String inputFilePath = "/API descriptions"; 
	static String outputFilePath = "/API Name and Descriptions";
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
	        	combineClassAndAPI(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to combine API Name and description
	 * @param fileName
	 * @throws IOException
	 */
	public static void combineClassAndAPI(String fileName) throws IOException{
		FileInputStream fisAPI = new FileInputStream(inputFilePath + "\\" + fileName);
		BufferedReader brAPI = new BufferedReader(new InputStreamReader(fisAPI));
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + fileName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String APILine = null;
		String[] APIArray = new String[5];
		
		String combinedResult = "";
		
		while ((APILine = brAPI.readLine()) != null){
			APIArray = APILine.split("###");
			if (APIArray.length > 1){
				combinedResult = splitCamelCase(APIArray[0]).trim() + " ";
				combinedResult += APIArray[1].trim() + " ";
				pwStream.println(APIArray[0] + "###" + combinedResult);
			}
		}
		pwStream.close();
		brAPI.close();
	}
	
	/**
	 * Split camel case of each API
	 * @param API
	 * @return
	 */
	public static String splitCamelCase(String API){
		String[] APINameArray = API.split("\\.");
		String resultingSentence = "", extractedAPIName = "";
		
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
		
		return resultingSentence;
	}
}
