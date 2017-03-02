/* This program sorts the cosine similarity in descending order for basic TF-IDF approach */
package CombineSimilarity;

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
	static String inputFilePath = "/API Name and Description results (weighted)";
	static String outputFilePath = "/API Name and Description results (sorted)";
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

		while ((line = br.readLine()) != null){
			lineArray = line.split("###");
			if (lineArray.length == 5 && lineArray[lineArray.length - 1].contains("0")){
					map.put(lineArray[0] + "###" + lineArray[1] + "###" + lineArray[2] + "###" + lineArray[3], Double.parseDouble(lineArray[lineArray.length - 1]));
			}
			else if (lineArray.length < 2){
				sorted_map.putAll(map);
				int count = 0;
				for(Map.Entry<String, Double> entry : sorted_map.entrySet()) {
					pwStream.println(entry.getKey() + "###" + entry.getValue());
					count++;
					if (count == 30) break;
				}
				
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
