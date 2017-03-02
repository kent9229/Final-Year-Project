/* This is the main program for modified TF-IDF for API description */
package ImprovedTFIDF;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TfIdfMain {
    public static void main(String args[]) throws FileNotFoundException, IOException {
    	String filePath = "/API Name and Descriptions (preprocessed)"; 
        DocumentParser dp = new DocumentParser();
        dp.buildVocab(filePath);
        dp.tfIdfCalculator(filePath); //calculates tfidf
    }
}
