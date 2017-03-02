/* This program combines all the APIs into a single file */
package Doc2Vec;

import java.io.*;

public class CombiningFiles {
	static String inputFilePath = "/API descriptions";
	static String outputFilePath = "/CombinedLibraries.txt"; 
    public static void main(String[] args) throws IOException {
        File[] files = new File(inputFilePath).listFiles();
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
                showFiles(file.listFiles());
            } else {
                extractData(file.getName());
            }
        }
    }

    /**
     * Combine all API descriptions into a single file
     * @param fileName
     * @throws IOException
     */
    public static void extractData(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFilePath + "\\" + fileName));
        FileWriter fwStream = new FileWriter(outputFilePath, true);
        BufferedWriter bwStream = new BufferedWriter(fwStream);
        PrintWriter pwStream = new PrintWriter(bwStream);

        String line = null;
        String[] lineArray = new String[20];

        while ((line = br.readLine()) != null) {
            lineArray = line.split("###");
            if (lineArray.length > 1 && !lineArray[1].trim().equals("") && !lineArray[1].trim().equals("\u00A0")) {
                pwStream.println(line);
            }
        }
        pwStream.close();
    }
}
