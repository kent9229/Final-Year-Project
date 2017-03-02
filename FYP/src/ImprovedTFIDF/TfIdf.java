/* This program computes the modified TF-IDF of a term for API description */
package ImprovedTFIDF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TfIdf {
    
    /**
     * Calculate the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(String[] totalterms, String termToCheck) {
    	HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();
    	double freq = 0, maxFreq = 0;  //to count the overall occurrence of the term termToCheck
    	
    	for (String term: totalterms){
        	if (wordFreq.containsKey(term.toLowerCase())){
        		int termFreq = wordFreq.get(term.toLowerCase());
        		wordFreq.put(term.toLowerCase(), termFreq + 1);
        	} else {
        		wordFreq.put(term.toLowerCase(), 1);
        	}
		}
    	
		for(Map.Entry<String, Integer> entry: wordFreq.entrySet()){
			if (entry.getValue() > maxFreq)
				maxFreq = entry.getValue();
	    }
    	
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
            	freq++;
            }
        }
        return (0.5 + ((0.5 * freq) / maxFreq));
    }
    
	/**
	 * Calculated the tf of term termToCheck
	 * @param wordFreq : Number of documents the term appears in
	 * @param NumOfDoc : Total number of documents
	 * @return IDF value
	 */
    public double idfCalculator(int docFreqOfWords, int NumOfDoc) {
        return Math.log(1 + (NumOfDoc / docFreqOfWords));
    }
}

