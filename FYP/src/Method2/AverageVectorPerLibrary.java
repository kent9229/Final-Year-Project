/*This program computes the average vectors of all the packages in the library */
package Method2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class AverageVectorPerLibrary {
	static String inputFilePath = "/Method 2\\Packages";
	public static void main(String[] args) throws IOException{
		File[] files = new File(inputFilePath).listFiles();
		showFiles(files);
	}
	
	/**
	 * Read all files in the directory and compute the average vector of each library
	 * @param files
	 * @throws IOException
	 */
	public static void showFiles(File[] files) throws IOException {
	   FileWriter fwStream = new FileWriter(inputFilePath + "\\Library Vectors.txt", true);
	   BufferedWriter bwStream = new BufferedWriter(fwStream);
	   PrintWriter pwStream = new PrintWriter(bwStream);
	   
	   double average = 0;
	   String[] libraryName = new String[10];
	   
	   for (File file : files) {
		   libraryName = file.getName().split("\\.txt");
		   pwStream.print(libraryName[0].split("\\.csv")[0] + " ");
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file.listFiles());
	        } else {
	        	for (int index = 1; index < 201; index++){
	        		average = computeAverage(file.getName(), index);
	        		pwStream.print(average + " ");
	        	}
	        }
	        pwStream.println();
	    }
	   pwStream.close();
	}
	
	/**
	 * Compute the average of all the vectors in the specified library
	 * @param fin
	 * @param index
	 * @return
	 * @throws IOException
	 */
	private static double computeAverage(String fin, int index) throws IOException {
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + fin);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String[] lineArray = new String[3];
		String line = null;
		int count = 0;
		double total = 0;
		
		while ((line = br.readLine()) != null) {
			lineArray = line.split(" ");
			total += Double.parseDouble(lineArray[index]);
			count++;
		}
		br.close();
		
		return total/count;
	}
	
}
