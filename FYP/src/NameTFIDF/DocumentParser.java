/* This program builds vocabulary to compute basic TF-IDF for API Name */
package NameTFIDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class DocumentParser {

	HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();	// Number of documents the term appear in
	HashMap<String, Double> vocab = new HashMap<String, Double>();	// Vocabulary (Key: words, Value: tfidf)
	int NumOfDoc = 0;	// Total Number of documents
	static ArrayList<String> alreadyDoneFiles = new ArrayList<String>();
	static String outputFilePath = "/API Name (TFIDF)";
	
    /**
     * Method to read files and store in array.
     * @param filePath : source file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void buildVocab(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        StringBuilder sb = new StringBuilder();
        String s = null;
        ArrayList<String> appearBeforeList = new ArrayList<String>();
        
        BufferedReader in = null;
        for (File f : allfiles) {
            in = new BufferedReader(new FileReader(f));
            while ((s = in.readLine()) != null) {
            	if (s.trim().split("###").length > 1 && !s.trim().split("###")[1].trim().equals("") &&  !s.trim().split("###")[1].trim().equals("\u00A0")) {
            		sb.append(s.split("###")[1]);
            	
	            	String[] tokenizedTerms = sb.toString().replaceAll("[^a-zA-Z0-9.\\s]", "").split("\\W+");   //to get individual terms
	                for (String term : tokenizedTerms) {
	                	if (wordFreq.containsKey(term.toLowerCase()) && !appearBeforeList.contains(term.toLowerCase())){
	                		wordFreq.put(term.toLowerCase(), wordFreq.get(term.toLowerCase()) + 1);
	                		appearBeforeList.add(term.toLowerCase());
	                	}
	                	else if (!appearBeforeList.contains(term.toLowerCase())){
	                		wordFreq.put(term.toLowerCase(), 1);
	                		appearBeforeList.add(term.toLowerCase());
	                	}
	                	vocab.put(term.toLowerCase(), 0.0);
	                }
	                Arrays.fill(tokenizedTerms, null);
	                sb = new StringBuilder();
            	}
            	appearBeforeList = new ArrayList<String>();
            	NumOfDoc++;
            }
        }    
	    
    }

    /**
     * Method to create termVector according to its tfidf score.
     * @throws IOException 
     */
    public void tfIdfCalculator(String filePath) throws IOException {
    	alreadyGenerated();
    	File[] allfiles = new File(filePath).listFiles();
    	StringBuilder sb = new StringBuilder();
    	double tf = 0, idf = 0;
    	String line = null, fullStr = "";
    	
    	BufferedReader in = null;
    	FileWriter fwStream = null;
		BufferedWriter bwStream = null;
		PrintWriter pwStream = null;
    	
    	for (File f : allfiles) {
    		if (!alreadyDoneFiles.contains(f.getName())){
	    		in = new BufferedReader(new FileReader(f));
	    		fwStream = new FileWriter("\\" + f.getName(), true);
	    		bwStream = new BufferedWriter(fwStream);
	    		pwStream = new PrintWriter(bwStream);
	    		
	    		while ((line = in.readLine()) != null) {
	    			if (line.trim().split("###").length > 1 && !line.trim().split("###")[1].trim().equals("") &&  !line.trim().split("###")[1].trim().equals("\u00A0")) {
	    				fullStr = line + "###";
	            		sb.append(line.split("###")[1]);
	            		String[] tokenizedTerms = sb.toString().replaceAll("[^a-zA-Z0-9.\\s]", "").split("\\W+");   //to get individual terms
	            		for (String eachTerm: tokenizedTerms){
	            			tf = new TfIdf().tfCalculator(tokenizedTerms, eachTerm);
	            			idf = new TfIdf().idfCalculator(wordFreq.get(eachTerm.toLowerCase()), NumOfDoc);
	            			vocab.put(eachTerm.toLowerCase(), tf * idf);
	            		}
	            		int count = 0;
	            		for(Entry<String, Double> entry: vocab.entrySet()){
	            			if (entry.getValue() != 0) 
	            				fullStr += count + "," + entry.getValue() + " ";
	            			
	            			count++;
	            		}
	            		if (!fullStr.equals(line + "###"))
	            			pwStream.println(fullStr);
	            		Arrays.fill(tokenizedTerms, null);
	                    sb = new StringBuilder();
	    			}
	    			for(Entry<String, Double> entry: vocab.entrySet()){
	        	    	vocab.put(entry.getKey(), 0.0);
	    			}
	    		}
	    		pwStream.close();
	    	}
    	}
    }
    
    public static void alreadyGenerated(){
    	File[] allfiles = new File(outputFilePath).listFiles();
    	int count = 0;
    	for (File f: allfiles){
    		alreadyDoneFiles.add(f.getName());
    		System.out.println(count++ + ": " + f.getName());
    	}
    }
}
