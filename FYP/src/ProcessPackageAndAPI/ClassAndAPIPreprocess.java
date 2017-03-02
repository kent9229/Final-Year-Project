/* This program preprocess package and API descriptions using the six preprocessing steps */
package ProcessPackageAndAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import PreprocessAPIDesc.Stemmer;
import PreprocessAPIDesc.StopWords;

public class ClassAndAPIPreprocess {
	public static String outputFilePath = "/API descriptions (preprocessed)\\";
	public void preprocess(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        StringBuilder sb = new StringBuilder();
        String s = null;
        
        BufferedReader in = null;
        FileWriter fwStream = null;
        BufferedWriter bwStream = null;
        PrintWriter pwStream = null;
        for (File f : allfiles) {
            in = new BufferedReader(new FileReader(f));
            fwStream = new FileWriter(outputFilePath + f.getName(), true);
    		bwStream = new BufferedWriter(fwStream);
    		pwStream = new PrintWriter(bwStream);
            while ((s = in.readLine()) != null) {
            	if (!s.trim().equals("\u00A0") && s.split("###").length > 1) {
            		sb.append(s.split("###")[1]);
            	
	            	String[] tokenizedTerms = sb.toString().replaceAll("[^a-zA-Z0-9.\\s]", "").split("\\W+");   //to get individual terms
	            	String resultingTerms = "";
	                for (String term : tokenizedTerms) {
	                	resultingTerms += preprocessTerm(term);
	                }
	                
	                pwStream.println(s.split("###")[0] + "###" + resultingTerms.trim());
	                Arrays.fill(tokenizedTerms, null);
	                sb = new StringBuilder();
	                
            	} else pwStream.println(s);
            }
            pwStream.close();
        }    
	    
    }
	
	/**
	 * Perform the six preprocessing steps
	 * @param word
	 * @return processed string
	 */
	public static String preprocessTerm(String word){
		String result = removeIdentifiers(word);
		result = splitPackageNotation(result);
		result = splitCamelCase(result);
		result = convertToLowerCase(result);
		result = stemming(result);
		return stopWordRemoval(result);
	}
	
	/**
	 * Presentation elements. Remove identifiers such as "@code"
	 * @param word Input word
	 * @return processed string
	 */
	public static String removeIdentifiers(String word){
		if (!word.contains("@"))
			return word;
		
		return "";
	}
	
	/**
	 * Split package notation such as "javax.microedition.lcdui"
	 * @param word Input word
	 * @return processed string
	 */
	public static String splitPackageNotation(String word){
		String[] wordArray = word.split("\\.");
		String result = "";
		
		if (wordArray.length > 1){
			for (int i = 0; i < wordArray.length; i++)
				result += wordArray[i] + " ";
			return result.trim();
		} else	return word;
	}
	
	/**
	 * Split camel case words
	 * @param word Input word
	 * @return processed string
	 */
	public static String splitCamelCase(String word){
		String result = "";
		
		for (String w : word.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
	        result += w + " ";
	    }
		
		return result.trim();
	}
	
	/**
	 * Convert word to lower case
	 * @param word Input word
	 * @return processed string
	 */
	public static String convertToLowerCase(String word){
		return word.toLowerCase();
	}
	
	/**
	 * Stemming: Transform word into their base form
	 * @param word Input word
	 * @return processed string
	 */
	public static String stemming(String word){
		Stemmer sm = new Stemmer();
		char[] result = word.toCharArray();
		
		sm.add(result, result.length);
		sm.stem();
		
		return sm.toString();
	}
	
	/**
	 * Remove stop words such as "a", "the", "and" etc.
	 * @param word Input word
	 * @return processed string
	 */
	public static String stopWordRemoval(String word){
		return StopWords.removeStopWords(word);
	}
}
