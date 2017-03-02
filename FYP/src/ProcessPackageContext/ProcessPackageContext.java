/* This program performs computation and sorts the package context information  */
package ProcessPackageContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ProcessPackageContext {
	static String filePath = "/Package Level";
	public static void main(String[] args) throws IOException{
		sortContextPackage();
	}
	
	/**
	 * Check for duplicate package sequences
	 * @throws IOException
	 */
	public static void checkDuplicate() throws IOException{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		FileInputStream fis = new FileInputStream(filePath + "\\Full Package Sequence (verified).txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter(filePath + "\\Full Package Sequence (remove duplicates).txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null; 
		
		int count = 1;
		while ((line = br.readLine()) != null) {
			if (map.containsKey(line.trim())){
				count = map.get(line) + 1;
				map.put(line.trim(), count);
			}
			else
				map.put(line.trim(), 1);
		}
		for(Map.Entry<String, Integer> entry: map.entrySet()){
			pwStream.println(entry.getKey());
	    }
		br.close();
		pwStream.close();
	}
	
	/**
	 * Remove packages without context
	 * @throws IOException
	 */
	public static void removeNoContext() throws IOException{
		FileInputStream fis = new FileInputStream(filePath + "\\Full Package Sequence (verified).txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter(filePath + "\\Full Package Sequence (with context).txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null; 
		String[] lineArray = null;
		
		while ((line = br.readLine()) != null) {
			lineArray = line.split(", ");
			if (lineArray.length > 1)
				pwStream.println(line);
		}
		br.close();
		pwStream.close();
	}
	
	/**
	 * Count the frequency of each package in the entire file
	 * @throws IOException
	 */
	public static void combineUniquePackage() throws IOException {
		FileInputStream fis = new FileInputStream(filePath + "\\Full Package Sequence (Final Context).txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter(filePath + "\\packageContext.txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
		
		HashMap<String, String> map = new HashMap<String, String>();
		String line = null;
		String[] lineArray = null;
		
		while ((line = br.readLine()) != null){
			lineArray = line.split("###");
			if (lineArray.length > 1){
				if (!map.containsKey(lineArray[0]))
					map.put(lineArray[0], lineArray[1]);
				else {
					String existingContextPkg = map.get(lineArray[0]);
					existingContextPkg += ", " + lineArray[1];
					map.put(lineArray[0], existingContextPkg);
				}
			}
		}
		
		for (Map.Entry <String, String> entry: map.entrySet())
			pwStream.println(entry.getKey() + "###" + entry.getValue());
		pwStream.close();
	}
	
	/**
	 * Find the context of each package
	 * @throws IOException
	 */
	public static void deriveContext() throws IOException{
		FileInputStream fis = new FileInputStream(filePath + "\\Full Package Sequence (with context).txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		FileWriter fwStream = new FileWriter(filePath + "\\Full Package Sequence (Final Context).txt", true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);
		
		String line = null; 
		String[] lineArray = null;
		String toPrint = "";
		while ((line = br.readLine()) != null) {
			lineArray = line.split(", ");
			for (int i = 0; i < lineArray.length; i++){
				toPrint = "";
				for (int j = 0; j < lineArray.length; j++){
					if (!lineArray[i].equals(lineArray[j])){
						if (toPrint.equals(""))
							toPrint = lineArray[j];
						else toPrint += ", " + lineArray[j];
					}
					
					if (j == lineArray.length - 1)
						pwStream.println(lineArray[i] + "###" + toPrint);
				}
				
			}
		}
		br.close();
		pwStream.close();
	}
	
	/**
	 * Sort the context packages according to their frequencies
	 * @throws IOException
	 */
	public static void sortContextPackage() throws IOException {
		FileInputStream fis = new FileInputStream(filePath + "\\packageContext.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
		
        FileWriter fwStream = new FileWriter(filePath + "\\packageContext (sorted).txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
        
		String line = null;
		String[] lineArray = null;
		String[] contextPkgArray = null;
		String toPrint = "";
		while ((line = br.readLine()) != null){
			lineArray = line.split("###");
			contextPkgArray = lineArray[1].split(", ");
			toPrint = "";
			
			for (String eachPkg: contextPkgArray){
				if (!map.containsKey(eachPkg))
					map.put(eachPkg, 1);
				else {
					int existCount = map.get(eachPkg) + 1;
					map.put(eachPkg, existCount);
				}
			}
			
			sorted_map.putAll(map);
			for(Map.Entry<String, Integer> entry : sorted_map.entrySet())
				if (toPrint.equals("")) toPrint = entry.getKey();
				else toPrint += ", " + entry.getKey();
			
			pwStream.println(lineArray[0] + "###" + toPrint);
			
			map.clear();
			sorted_map.clear();
		}
		pwStream.close();
	}
}

class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
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
