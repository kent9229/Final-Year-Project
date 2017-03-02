/*
 * This program inserts the library's name of each package in the package sequence
 * next to the package's name
 */
package DataProcessing;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class InsertLibrariesIntoPackageSequence {
	static String inputFilePath = "/Raw Data.txt";
	static String libraryFilePath = "/Data-Labels";
	static String outputFilePath = "/Data-Labels";
	public static void main(String[] args) throws IOException{
		showFiles();
	}
	
	/**
	 * To loop through a directory to process all files
	 * @throws IOException
	 */
	public static void showFiles() throws IOException{
		FileInputStream fis = new FileInputStream(inputFilePath);
		File[] files = new File(libraryFilePath).listFiles();
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		FileWriter fwStream = new FileWriter(outputFilePath, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		int lineNum = 1; 
		String line = null;
		String[] lineArray = new String[1000];
		String[] libraryName;
		boolean emptyLine = true;
		
		while ((line = br.readLine()) != null) {
			lineArray = line.split(", ");
			for (File file : files) {
				libraryName = file.getName().split(" ");
		        if (file.isDirectory()) {
		            System.out.println("Directory: " + file.getName());
		            showFiles();
		        } else {
					for (int i = 0; i < lineArray.length; i++){
						emptyLine = addLibrary(file.getName(), lineArray[i].trim());
						if (i != lineArray.length - 1 && emptyLine == false){
							pwStream.print(", ");
						}
						if (i == lineArray.length - 1 && emptyLine == false){
							pwStream.println();
						}
					}
		        }
			}
			lineNum++;
		}
		br.close();
		pwStream.close();
	}
	
	/**
	 * Add each library's name next to the package's name 
	 * @param fin
	 * @param packageName
	 * @return
	 * @throws IOException
	 */
	private static boolean addLibrary(String fin, String packageName) throws IOException {
		FileInputStream fis = new FileInputStream(libraryFilePath + "\\" + fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		FileWriter fwStream = new FileWriter(libraryFilePath + "\\Raw Data (libraries).txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
	 
		String line = null;
		String[] libraryName = new String[5];
		boolean emptyLine = true;
		libraryName = fin.split(" ");
		
		while ((line = br.readLine()) != null) {
			if (packageName.toLowerCase().trim().equals(line.trim())){
				pwStream.print(packageName.toLowerCase().trim() + ", " + libraryName[0].trim());
				emptyLine = false;
				break;
			}
		}
		br.close();
		pwStream.close();
		return emptyLine;
	}
}
