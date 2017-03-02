/* This program uses hashmap mainly to categorise the packages into their libraries */
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HashMapApp {
	static ArrayList<String> packageLibrariesCombine = new ArrayList<String>();
	static String filePath = "/Package descriptions";
	public static void main(String[] args) throws IOException{
		File[] files = new File(filePath).listFiles();
		showFiles(files);
		putInHashMap();
	}
	
	/**
	 * Run through the directory folder
	 * @param files
	 * @throws IOException
	 */
	public static void showFiles(File[] files) throws IOException {
	   for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file.listFiles()); // Calls same method again.
	        } else {
	            readFile(file.getName());
	        }
	    }
	}
	
	/**
	 * Read each file, map each class to its respective libraries and write to file
	 * @param fin
	 * @throws IOException
	 */
	private static void readFile(String fin) throws IOException {
		FileInputStream fis = new FileInputStream(filePath + "\\" + fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		String[] libraryName = new String[100];
		
		while ((line = br.readLine()) != null) {
			libraryName = fin.split("\\.csv");
			packageLibrariesCombine.add(line + " => " + libraryName[0].trim());
		}
		
		br.close();
	}
	
	/**
	 * Run through the folder directory
	 * @throws IOException
	 */
	public static void putInHashMap() throws IOException {
		
		FileWriter fwStream = new FileWriter(filePath + "\\packageMappings.txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream); 
		
		HashMap<String, String> map = new HashMap<String, String>();
    	
		for (String line: packageLibrariesCombine){
			//map.put(line.split(" => ")[0].trim(), line.split(" => ")[1].trim());
			pwStream.println(line.split(" => ")[1].trim() + "###" + line.split(" => ")[0].trim());
		}
		pwStream.close();
	    /*
	    for(Map.Entry<String, String> entry: map.entrySet()){
	    	System.out.println(entry.getKey()+ " ===== "+ entry.getValue());
	    }
	    */
	    //System.out.println("Count: " + count);
	    //checkIfCorrectLibrary(map);
	    //hashSearch2D(map);
		//separate200DPackages(map);
	    //separatePackagesIntoLibraries(map);
	    System.out.println("Done!");
	}
	
	private static void separatePackagesIntoLibraries(HashMap<String, String> map) throws IOException {
		FileInputStream fis = new FileInputStream(filePath + "\\packageContext (sorted).txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = null;
		BufferedWriter bwStream = null;
		PrintWriter pwStream = null;
		
		String[] lineArray = null;
		String line = null;

		while ((line = br.readLine()) != null) {
			lineArray = line.split("###");
			if (map.containsKey((lineArray[0].trim()))){
				fwStream = new FileWriter(filePath + "\\Package Context\\" + map.get(lineArray[0].trim()) + ".txt", true);
				bwStream = new BufferedWriter(fwStream);
				pwStream = new PrintWriter(bwStream);
				pwStream.println(line.trim());
				pwStream.close();
			}
		}
		br.close();
	}
	
	/**
	 * Check if the results of the recommendation belongs to a library
	 * @param map
	 * @throws IOException
	 */
	private static void checkIfCorrectLibrary(HashMap<String, String> map) throws IOException {
		FileInputStream fis = new FileInputStream("/Jackson vs Gson.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter("/Jackson vs Gson (verified).csv", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null;
		
		int count = 1;
		while ((line = br.readLine()) != null){
			if (line.split(",").length != 2){
				pwStream.println(line);
			}
			else{
				for(Map.Entry<String, String> entry: map.entrySet()){
					//System.out.println(entry.getKey() + ": " + entry.getValue());
					if (map.get(line.split(",")[0].toLowerCase().trim()) != null && map.get(line.split(",")[0].toLowerCase().trim()).equals("gson")){
						pwStream.println(line);
						break;
					}
				}
			}
			count++;
		}
		pwStream.close();
		br.close();
	}
	
	/**
	 * Insert libraries into package sequence using hashmap
	 * @param map
	 * @throws IOException
	 */
	public static void hashSearch2D(HashMap<String, String> map) throws IOException{
		FileInputStream fis = new FileInputStream("/Raw Data.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter("/Raw Data (libraries).txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		String[] lineArray = new String[500];
		String line = null;
		String[] tempList = new String[1000];
		int tempIndex = 0;
		boolean alreadyExist = false;
		
		int count = 0;

		while ((line = br.readLine()) != null) {
			lineArray = line.split(", ");
			for (int i = 0; i < lineArray.length; i++){
				alreadyExist = false;
				for(Map.Entry<String, String> entry: map.entrySet()){
					for (int j = 0; j < tempList.length; j++){
						if (tempList[j] != null && map.get(lineArray[i].toLowerCase().trim().toString()) != null && tempList[j].equals(map.get(lineArray[i].toLowerCase().trim()).toString()))
							alreadyExist = true;
					}	
					if (map.containsKey((lineArray[i].toLowerCase().trim())) && alreadyExist == false){
						pwStream.print(lineArray[i].toLowerCase().trim() + ", " + map.get(lineArray[i].toLowerCase().trim()));
						if (map.get(lineArray[i].toLowerCase().trim()).toString() != null){
							//System.out.println(tempIndex + ": " + map.get(lineArray[i].toLowerCase().trim()).toString());
							tempList[tempIndex++] = map.get(lineArray[i].toLowerCase().trim()).toString();
						}
						if (i != lineArray.length - 1){
							pwStream.print(", ");
						}
						break;
					}
					else {
						pwStream.print(lineArray[i].trim());
						if (i != lineArray.length - 1){
							pwStream.print(", ");
						}
						break;
					}
				}	
			}
			pwStream.println();
			
			Arrays.fill(tempList, null);
			tempIndex = 0;
			//alreadyExist = false;
		}
		pwStream.close();
	}
	
	/**
	 * Splits the 200 dimensions of each package into their respective libraries
	 * @param map
	 * @throws IOException
	 */
	private static void separate200DPackages(HashMap<String, String> map) throws IOException {
		FileInputStream fis = new FileInputStream("/200D.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = null;
		BufferedWriter bwStream = null;
		PrintWriter pwStream = null;
		
		String[] lineArray = new String[500];
		String line = null;

		while ((line = br.readLine()) != null) {
			lineArray = line.split(" ");
			for(Map.Entry<String, String> entry: map.entrySet()){
				if (map.containsKey((lineArray[0].toLowerCase().trim()))){
					fwStream = new FileWriter("/" + map.get(lineArray[0].toLowerCase().trim()) + ".txt", true);
					bwStream = new BufferedWriter(fwStream);
					pwStream = new PrintWriter(bwStream);
					pwStream.println(line.trim());
					pwStream.close();
					break;
				}
			}
		}
		br.close();
	}
}
