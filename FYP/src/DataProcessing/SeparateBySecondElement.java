/* This program splits up the package sequences based on the second element 
 * since many packages' second element depicts the library's name */
package DataProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SeparateBySecondElement {
	static String inputFilePath = "/tsne-2D.txt";
	static String outputFilePath = "/Data";
	public static void main(String[] args) throws IOException{
		readFile(inputFilePath);
	}
	
	/**
	 * Split the packages using the second element of each package's name
	 * @param fin
	 * @throws IOException
	 */
	private static void readFile(String fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = null;
		
		String[] lineArray = new String[3];
		String[] wordArray = new String[3];
		String line = null;
		
		while ((line = br.readLine()) != null) {
			lineArray = line.split(",");
			wordArray = lineArray[2].split("\\.");
			if (!wordArray[1].trim().equals("*")) fwStream = new FileWriter(outputFilePath + "\\" + wordArray[1].trim() + ".txt", true);
			else fwStream = new FileWriter(outputFilePath + "\\Others.txt", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			pwStream.println(line);
			pwStream.close();
		}
		br.close();
	}
}
