/*This program sorts the cosine similarity in descending order for Method 2 */
package Method2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SortCosineSimilarity {
	static String filePath = "/Method 2";
	public static void main(String[] args) throws IOException{
		sorting("Opennlp", "Stanford-nlp");
	}
	
	/**
	 * To sort the cosine similarity in hashmap/treemap
	 * @param libraryName
	 * @param targetLibrary
	 * @throws IOException
	 */
	public static void sorting(String libraryName, String targetLibrary) throws IOException{
		FileInputStream fis = new FileInputStream(filePath + "\\" + libraryName + " vs " + targetLibrary + ".csv");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		
        FileWriter fwStream = new FileWriter(filePath + "\\" + libraryName + " vs " + targetLibrary + " (sorted).csv", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
        
		String line = null;
		String[] lineArray = new String[10];
		while ((line = br.readLine()) != null){
			lineArray = line.split(",");
			if (lineArray.length == 2){
				map.put(lineArray[0], Double.parseDouble(lineArray[1]));
			}
			else if (lineArray.length < 2){
				sorted_map.putAll(map);
				for(Map.Entry<String, Double> entry : sorted_map.entrySet()) {
					pwStream.println(entry.getKey() + "," + round(entry.getValue(), 4));
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
