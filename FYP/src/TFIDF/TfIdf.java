/* This program computes the basic TF-IDF of a term for API description */
package TFIDF;

public class TfIdf {
    
    /**
     * Calculate the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(String[] totalterms, String termToCheck) {
        double freq = 0;  // To count the overall occurrence of the term termToCheck
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
            	freq++;
            }
        }
        return freq / totalterms.length;
    }
    
	/**
	 * Calculated the idf of term termToCheck
	 * @param wordFreq : Number of documents the term appears in
	 * @param NumOfDoc : Total number of documents
	 * @return IDF value
	 */
    public double idfCalculator(int docFreqOfWords, int NumOfDoc) {
        return Math.log(NumOfDoc / docFreqOfWords);
    }
}

