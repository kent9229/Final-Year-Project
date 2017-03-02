/* This program sorts the cosine similarity in descending order for 21 iterations */
package FindWeight;

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
	static String outputFilePath = "/API descriptions doc2vec results (sorted)";
	public static void main(String[] args) throws IOException{
		double a = 0.0;
		while (CombineCosineSimilarity.round(a,2) <= 1.0){
			File[] files = new File(inputFilePath + "\\" + CombineCosineSimilarity.round(a, 2)).listFiles();
			showFiles(files, CombineCosineSimilarity.round(a,2));
			a += 0.05;
		}
	}
	
	/**
	 * To loop through a directory to process all files
	 * @param files
	 * @param weight
	 * @throws IOException
	 */
	public static void showFiles(File[] files, double weight) throws IOException {
	   for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file.listFiles(), weight); // Calls same method again.
	        } else {
	        	sorting(file.getName(), weight);
	        }
	    }
	}
	
	/**
	 * To sort the cosine similarity in hashmap/treemap
	 * @param libraryName
	 * @param targetLibrary
	 * @throws IOException
	 */
	public static void sorting(String fileName, double weight) throws IOException{
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + weight + "\\" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		
        FileWriter fwStream = new FileWriter("C:\\Users\\Kent Ong\\Dropbox\\CSC\\FYP\\Data\\Crawler\\API Name and Description results (sorted)\\" + weight + "\\" + fileName, true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
        
		String line = null;
		String[] lineArray = new String[10];

		while ((line = br.readLine()) != null){
			lineArray = line.split("###");
			if (lineArray.length == 4 && lineArray[lineArray.length - 1].contains("0")){
					map.put(lineArray[0] + "###" + lineArray[1] + "###" + lineArray[2], Double.parseDouble(lineArray[lineArray.length - 1]));
			}
			else if (lineArray.length < 2){
				sorted_map.putAll(map);
				for(Map.Entry<String, Double> entry : sorted_map.entrySet()) {
					pwStream.println(entry.getKey() + "###" + entry.getValue());
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