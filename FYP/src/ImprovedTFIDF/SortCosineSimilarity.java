/* This program sorts the cosine similarity in descending order for modified TF-IDF 
 * for API description */
package ImprovedTFIDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SortCosineSimilarity {
	static String inputFilePath = "/API Name and Descriptions results";
	static String outputFilePath = "/API Name and Descriptions results (sorted)";
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
	        	sorting(file.getName());
	        }
	    }
	}
	
	/**
	 * To sort the cosine similarity in hashmap/treemap
	 * @param libraryName
	 * @param targetLibrary
	 * @throws IOException
	 */
	public static void sorting(String fileName) throws IOException{
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		
        FileWriter fwStream = new FileWriter(outputFilePath + "\\" + fileName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
        
		String line = null;
		String[] lineArray = new String[10];
		int count = 0;
		while ((line = br.readLine()) != null){
			lineArray = line.split("###");
			if (lineArray.length == 3){
				map.put(lineArray[0] + "###" + lineArray[1], Double.parseDouble(lineArray[lineArray.length - 1]));
			}
			else if (lineArray.length < 2){
				sorted_map.putAll(map);
				for(Map.Entry<String, Double> entry : sorted_map.entrySet()) {
					pwStream.println(entry.getKey() + "###" + round(entry.getValue(), 4));
					count++;
				}
				
				count = 0;
				map.clear();
				sorted_map.clear();
				pwStream.println();
			}
			else {
				pwStream.println(line);
			}
		}
		pwStream.close();
	}
	
	/**
	 * To round a value to the specified number of decimal places
	 * @param value
	 * @param places
	 * @return rounded value
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}

// Comparator class
class ValueComparator implements Comparator<String> {
    Map<String, Double> base;

    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}
