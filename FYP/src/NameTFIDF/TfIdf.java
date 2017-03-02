/* This program computes the basic TF-IDF of a term for API Name */
package NameTFIDF;

public class TfIdf {
    
    /**
     * Calculated the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(String[] totalterms, String termToCheck) {
        double freq = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
            	freq++;
            }
        }
        return freq / totalterms.length;
    }
    
    /**
     * Calculated idf of term termToCheck
     * @param allTerms : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    public double idfCalculator(int docFreqOfWords, int NumOfDoc) {
        return Math.log(NumOfDoc / docFreqOfWords);
    }
}

