/* This class is combine the name and description of each package and API */
package ProcessPackageAndAPI;

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
	        	combineClassAndAPI(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to combine package/API Name and description
	 * @param fileName
	 * @throws IOException
	 */
	public static void combineClassAndAPI(String fileName) throws IOException{
		FileInputStream fisAPI = new FileInputStream(filePath + "API descriptions\\" + fileName);
		BufferedReader brAPI = new BufferedReader(new InputStreamReader(fisAPI));
		
		FileInputStream fisClass = new FileInputStream(filePath + "Package Level\\Package descriptions\\" + fileName);
		BufferedReader brClass = new BufferedReader(new InputStreamReader(fisClass));
		
		FileWriter fwStream = null;
		BufferedWriter bwStream = null;
		PrintWriter pwStream = null;
		
		ArrayList<String> classLibrary = new ArrayList<String>();
		String classLine = null, combinedClassAndAPI = "";
		
		while ((classLine = brClass.readLine()) != null){
			classLibrary.add(classLine);
		}
		brClass.close();

		HashMap<String, String> map = null;
		
		String APILine = null;
		String[] APIArray = new String[5];
		
		while ((APILine = brAPI.readLine()) != null){
			APIArray = APILine.split("###");
			if (APIArray.length > 1){
				for (String eachTerm: classLibrary){
					String[] eachTermArray = eachTerm.split("###");
					if (APILine.contains(eachTermArray[0])){
						if (eachTermArray.length > 1){
							fwStream = new FileWriter(filePath + "Combined Package And API\\" + APIArray[0] + ".txt", true);
							bwStream = new BufferedWriter(fwStream);
							pwStream = new PrintWriter(bwStream);
							pwStream.println(eachTermArray[0].trim());
							pwStream.println(eachTermArray[1].trim());
							pwStream.println(APIArray[0].replace(eachTermArray[0] + ".", " ").trim());
							pwStream.println(APIArray[1].trim());
							pwStream.close();
							break;
						}
					}
				}
			}
		}
		
		brAPI.close();
	}
	
	/**
	 * Split camel case of each API
	 * @param API
	 * @return processed string
	 */
	public static String splitCamelCase(String API){
		String[] APINameArray = API.split("\\.");
		String resultingSentence = "";
		
		for (String eachTerm: APINameArray) {
			for (String w : eachTerm.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
				resultingSentence += w + " ";
		    }
		}
		
		return resultingSentence;
	}
}
