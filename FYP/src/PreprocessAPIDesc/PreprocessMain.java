/* This is the main program to preprocess the API descriptions */
package PreprocessAPIDesc;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PreprocessMain {
    public static void main(String args[]) throws FileNotFoundException, IOException {
    	String filePath = "/API Name and Descriptions"; 
        APIDescPreprocess pp = new APIDescPreprocess();
        pp.preprocess(filePath);
    }
}
