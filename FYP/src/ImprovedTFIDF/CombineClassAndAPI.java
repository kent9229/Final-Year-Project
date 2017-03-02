/* This program combines the API and packages information */
package ImprovedTFIDF;

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

public class CombineClassAndAPI {
	static String apiDescFilePath = "/API descriptions";
	static String packageDescFilePath = "/Package descriptions";
	static String outputFilePath = "/API Name and Descriptions";
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
	        	combineClassAndAPI(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to combine API and package information
	 * @param fileName
	 * @throws IOException
	 */
	public static void combineClassAndAPI(String fileName) throws IOException{
		FileInputStream fisAPI = new FileInputStream(apiDescFilePath + "\\" + fileName);
		BufferedReader brAPI = new BufferedReader(new InputStreamReader(fisAPI));
		
		FileInputStream fisClass = new FileInputStream(packageDescFilePath + "\\" + fileName);
		BufferedReader brClass = new BufferedReader(new InputStreamReader(fisClass));
		
		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + fileName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		ArrayList<String> classLibrary = new ArrayList<String>();
		String classLine = null, combinedClassAndAPI = "";
		
		while ((classLine = brClass.readLine()) != null){
			classLibrary.add(classLine);
		}
		brClass.close();
		
		HashMap<String, String> map = null;
		
		String APILine = null;
		String[] APIArray = new String[5];
		
		String API = "";
		String combinedResult = "";
		
		while ((APILine = brAPI.readLine()) != null){
			APIArray = APILine.split("###");
			if (APIArray.length > 1){
				combinedResult = splitCamelCase(APIArray[0]).trim() + " ";
				combinedResult += APIArray[1].trim() + " ";
				for (String eachTerm: classLibrary){
					if (APILine.contains(eachTerm.split("###")[0])){
						if (eachTerm.split("###").length > 1){
							combinedResult += eachTerm.split("###")[1];
							break;
						}
					}
				}
				pwStream.println(APIArray[0] + "###" + combinedResult);
			}
		}
		pwStream.close();
		brAPI.close();
	}
	
	/**
	 * Split the camel case of each API name
	 * @param API
	 * @return
	 */
	public static String splitCamelCase(String API){
		String[] APINameArray = API.split("\\.");
		String resultingSentence = "";
		
		for (String eachTerm: APINameArray) {
			for (int i = 0; i < eachTerm.length(); i++){
				if (i + 1 != eachTerm.length() && Character.isUpperCase(eachTerm.charAt(i+1)))
					resultingSentence += eachTerm.charAt(i) + " ";
				else resultingSentence += eachTerm.charAt(i) + "";
			}
			resultingSentence += " ";
		}
		
		return resultingSentence;
	}
}
