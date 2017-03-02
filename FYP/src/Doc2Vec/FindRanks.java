/* This program identifies the rank of each analogical API using the ground truth for doc2vec approach */
package Doc2Vec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindRanks {
	 static String evaluationFilePath = "/Evaluation";
	 static String filePath = "/API descriptions doc2vec results (sorted)";
	 public static void main(String[] args) throws IOException {
        File[] files = new File(evaluationFilePath).listFiles();
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
	                showFiles(file.listFiles()); // Calls same method again.
	            } else {
	                extractData(file.getName());
	            }
	        }
	    }
	    
	    /**
	     * Extract the ground truth into an array list
	     * @param fileName
	     * @throws IOException
	     */
	    public static void extractData(String fileName) throws IOException {
	        BufferedReader brMatchingAPI = new BufferedReader(new FileReader(evaluationFilePath + "\\" + fileName));
	        
	        ArrayList<String> AL = new ArrayList<String>();
	        String matchingAPIline = null;
	        String[] matchingAPIlineArray = new String[2];
	        while ((matchingAPIline = brMatchingAPI.readLine()) != null) {
	        	matchingAPIlineArray = matchingAPIline.split(" vs ");
	        	AL.add(matchingAPIlineArray[0] + "###" + matchingAPIlineArray[1]);
	        }
	        
	        brMatchingAPI.close();
	        FindRanks(AL, fileName);
	        
	    }
	    
	    /**
	     * Main method to find the ranks of analogical APIs using the ground truth
	     * @param AL
	     * @param fileName
	     * @throws IOException
	     */
	    public static void FindRanks(ArrayList<String> AL, String fileName) throws IOException{
	    	System.out.println(fileName);
	    	BufferedReader br = new BufferedReader(new FileReader(filePath + "\\" + fileName.split("\\.txt")[0] + ".csv"));
	    	HashMap<String, Integer> mapRanks = null;
	    	String line = null;
	    	String[] lineArray = new String[10];
	    	
	    	FileWriter fwStream = new FileWriter(filePath + "\\MRR\\" + fileName.split("\\.txt")[0] + " (FindRanks).txt", true);
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
			    			if (lineArray.length == 4){
			    				if (matchAPI.split("###")[0].equals(lineArray[0])){
			    					mapRanks = new HashMap<String, Integer>();
					    			sourceResult = matchAPI.split("###")[0].trim();
					    			targetResult = matchAPI.split("###")[1].trim();
					    			rank = 1;
			    				}
			    			}
				    		
				    		if (lineArray.length == 3 && mapRanks != null){
				    			mapRanks.put(lineArray[0].substring(lineArray[0].indexOf(")") + 1).trim(), rank);
				    			rank++;
				    		}
				    		
				    		if (mapRanks != null && i == sortedResult.size() - 1){
				    			//System.out.println(sourceResult + "\t" + targetResult + "\t" + mapRanks.get(targetResult));
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
