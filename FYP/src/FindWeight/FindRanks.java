/* This program identifies the rank of each analogical API using the ground truth for 21 iterations */
package FindWeight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindRanks {
	static String evaluationFilePath = "/Evaluation";
	 static String filePath = "/API Name and Description results (sorted)";
	 public static void main(String[] args) throws IOException {
		double a = 0.0;
		while (CombineCosineSimilarity.round(a,2) <= 1.0){
			File[] files = new File(evaluationFilePath).listFiles();
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
	                showFiles(file.listFiles(), weight);
	            } else {
	                extractData(file.getName(), weight);
	            }
	        }
	    }
	    
	    /**
	     * Extract the ground truth into an array list
	     * @param fileName
	     * @param weight
	     * @throws IOException
	     */
	    public static void extractData(String fileName, double weight) throws IOException {
	        BufferedReader brMatchingAPI = new BufferedReader(new FileReader(evaluationFilePath + "\\Evaluation\\" + fileName));
	        
	        ArrayList<String> AL = new ArrayList<String>();
	        String matchingAPIline = null;
	        String[] matchingAPIlineArray = new String[2];
	        while ((matchingAPIline = brMatchingAPI.readLine()) != null) {
	        	matchingAPIlineArray = matchingAPIline.split(" vs ");
	        	AL.add(matchingAPIlineArray[0] + "###" + matchingAPIlineArray[1]);
	        }
	        
	        brMatchingAPI.close();
	        FindRanks(AL, fileName, weight);
	        
	    }
	    
	    /**
	     * Main method to find the ranks of analogical APIs using the ground truth
	     * @param AL
	     * @param fileName
	     * @param weight
	     * @throws IOException
	     */
	    public static void FindRanks(ArrayList<String> AL, String fileName, double weight) throws IOException{
	    	BufferedReader br = new BufferedReader(new FileReader(filePath + "\\" + weight + "\\" + fileName.split("\\.txt")[0] + ".csv"));
	    	HashMap<String, Integer> mapRanks = null;
	    	String line = null;
	    	String[] lineArray = new String[10];
	    	
	    	FileWriter fwStream = new FileWriter(filePath + "\\" + weight + "\\MRR\\" + fileName.split("\\.txt")[0] + " (FindRanks).txt", true);
	        BufferedWriter bwStream = new BufferedWriter(fwStream);
	        PrintWriter pwStream = new PrintWriter(bwStream);
	    	
	    	String sourceResult = "";
	    	String targetResult = "";
	    	
	    	ArrayList<String> sortedResult = new ArrayList<String>();
	    	
	    	HashMap<String, String> mapResults = new HashMap<String, String>();	// To remove duplicate lines
	    	int rank = 1;
	    	while ((line = br.readLine()) != null){
	    		lineArray = line.split("###");
	    		if (lineArray.length > 1){
	    			sortedResult.add(line);
	    		}
	    		else {
			    	for (String matchAPI: AL){
				    	for (int i = 0; i < sortedResult.size(); i++){
				    		lineArray = sortedResult.get(i).split("###");
				    		if (lineArray.length == 4 && !lineArray[lineArray.length - 1].contains("0")){
			    				if (matchAPI.split("###")[0].equals(lineArray[0])){
			    					mapRanks = new HashMap<String, Integer>();
					    			sourceResult = matchAPI.split("###")[0].trim();
					    			targetResult = matchAPI.split("###")[1].trim();
					    			rank = 1;
			    				}
			    			}
				    	
				    		if (lineArray.length == 4 && lineArray[lineArray.length - 1].contains("0") && mapRanks != null){
				    			mapRanks.put(lineArray[0], rank);
				    			rank++;
				    		}
				    		
				    		if (mapRanks != null && i == sortedResult.size() - 1){
				    			if (mapRanks.get(targetResult) != null){
					    			mapResults.put(sourceResult + "\t" + targetResult + "\t" + mapRanks.get(targetResult), "");
					    			sourceResult = "";
					    			targetResult = "";
					    			mapRanks = null;
				    			}
				    		}
				    	}
			    	}
			    		sortedResult = new ArrayList<String>();
			    }
	    	}
	    	
	    	for(Map.Entry<String, String> entry: mapResults.entrySet()){
		    	pwStream.println(entry.getKey());
		    }
	    	
	    	pwStream.close();
    	}

}
