/* This is the main program to process the package and API information */
package ProcessPackageAndAPI;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PreprocessMain {
    public static void main(String args[]) throws FileNotFoundException, IOException {
    	String filePath = "/API descriptions"; 
        ClassAndAPIPreprocess pp = new ClassAndAPIPreprocess();
        pp.preprocess(filePath);
    }
}
