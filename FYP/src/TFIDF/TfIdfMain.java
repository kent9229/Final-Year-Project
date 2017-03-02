/* This is the main program for basic TF-IDF for API description */
package TFIDF;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TfIdfMain {
    public static void main(String args[]) throws FileNotFoundException, IOException {
    	String filePath = "/API descriptions"; 
        DocumentParser dp = new DocumentParser();
        dp.buildVocab(filePath);
        dp.tfIdfCalculator(filePath); //calculates tfidf
    }
}
