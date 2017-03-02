/* This program combines all package context information into a single file */
package ProcessPackageContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CombineAllFilesInOne {
	static String filePath = "\\Package Level";
	public static void main(String[] args) throws IOException {
		File[] files = new File(filePath + "\\Package Context").listFiles();
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
	            showFiles(file.listFiles()); // Calls same method again.
	        } else {
	        	System.out.println(file.getName());
	        	CombineAllFilesInOne(file.getName());
	        }
	    }
	}
	
	/**
	 * Main method to combine all files into a single file
	 * @param fileName
	 * @throws IOException
	 */
	public static void CombineAllFilesInOne(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(filePath + "\\Package Context\\" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter(filePath + "\\packageContext.txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null;
		pwStream.println(fileName.split("\\.txt")[0]);
		while ((line = br.readLine()) != null){
			pwStream.println(line);
		}
		pwStream.println();
		pwStream.close();
	}
}
