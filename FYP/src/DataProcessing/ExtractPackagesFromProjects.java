/* This program extracts packages from java files into package sequence 
 * and consolidate them into a single file
 */
package DataProcessing;
import java.util.regex.*;

import java.io.*;

public class ExtractPackagesFromProjects {
   static String filePath = "/Java Source Codes";
   public static void main(String[] args) throws IOException {
	   String folderName = "source_code86-90";
	   File[] files = new File(filePath + "\\" + folderName).listFiles();
	   showFiles(files, folderName);
   }
   
   /**
    * To loop through a directory to process all files
    * @param files
    * @param folderName
    * @throws IOException
    */
   public static void showFiles(File[] files, String folderName) throws IOException {
	    for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file.listFiles(), folderName); // Calls same method again.
	        } else {
	            extractData(file.getName(), folderName);
	        }
	    }
	}
   /**
    * Extract the packages from each java file
    * @param fileName
    * @param folderName
    * @throws IOException
    */
   public static void extractData(String fileName, String folderName) throws IOException{
	  Pattern patt = Pattern.compile("^import (.*)");
      BufferedReader br = new BufferedReader(new FileReader(filePath + "\\" + folderName + "\\" + fileName));
      
      String line, textLib = "", text = "";
      String[] lineArray = new String[100];
      int count = 0;
      
      while ((line = br.readLine()) != null) {
    	 lineArray = line.split(" ");
    	 Matcher m = patt.matcher(line);
    	 
         while (m.find()) {
        	lineArray = m.group(1).split(" ");
        	if (lineArray.length > 1){
        		text = lineArray[0].trim().replace("static", "").trim();
        		if (text.contains("//"))
        			text = text.substring(0, text.indexOf('/') - 1).trim();
        	}
        	else { 
        		text = m.group(1).trim().replace("static", "").trim();
        		if (text.contains("//"))
        			text = text.substring(0, text.indexOf('/') - 1).trim();
        	}
        	if (!text.trim().equals("")){
	        	if (count == 0) textLib = text.replace(";", "");
	        	else if (!textLib.equals("")) textLib += ", " + text.replace(";", "");
        	}
        	count++;
         }
      }
	FileWriter fwStream = new FileWriter(filePath + "\\" + folderName + ".txt", true);
	BufferedWriter bwStream = new BufferedWriter(fwStream);
	PrintWriter pwStream = new PrintWriter(bwStream);
	if (textLib != "") pwStream.println(fileName + "\t" + textLib);
	pwStream.close();
   }
}