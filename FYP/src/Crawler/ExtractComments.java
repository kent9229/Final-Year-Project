/* This program extracts the comments from each Java source files */
package Crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractComments {
	static String inputFilePath = "/Java Source Codes";
	public static void main(String[] args) throws IOException{
		String folderName = "source_code86-90";
		File[] files = new File(inputFilePath + "\\" + folderName).listFiles();
		
		FileWriter fwStream = new FileWriter(inputFilePath + "\\" + folderName + ".txt", true);
		BufferedWriter bwStream = new BufferedWriter(fwStream);
		PrintWriter pwStream = new PrintWriter(bwStream);
		
		showFiles(files, pwStream, folderName);
	}
	
	/**
	 * To loop through a directory to process all files
	 * @param files
	 * @param pwStream
	 * @param folderName
	 * @throws IOException
	 */
	public static void showFiles(File[] files, PrintWriter pwStream, String folderName) throws IOException {
		   for (File file : files) {
		        if (file.isDirectory()) {
		            System.out.println("Directory: " + file.getName());
		            showFiles(file.listFiles(), pwStream, folderName); // Calls same method again.
		        } else {
		        	readFile(file.getName(), pwStream, folderName);
		        }
		    }
		   pwStream.close();
	}
	
	/**
	 * Read each file in the folder
	 * @param fin
	 * @param pwStream
	 * @param folderName
	 * @throws IOException
	 */
	private static void readFile(String fin, PrintWriter pwStream, String folderName) throws IOException {
		FileInputStream fis = new FileInputStream(inputFilePath + "\\" + folderName + "\\" + fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String line = null;
		String comments = "";
		int index = 0, count = 0;
		String textLib = "";
		boolean description = false, commentsPrinted = false, packages = false;
		while ((line = br.readLine()) != null) {
			if (line.contains("/*") && !commentsPrinted){
				line = line.replace("/*", "").replace("*/", "");
				while (line.trim().length() > 0 && line.trim().charAt(index) == '*'){
					line = line.replace("*", "");
				}
				comments += line.trim();
				description = true;
			}
			else if (line.contains("*/")){
				if (!comments.trim().equals(""))
					pwStream.print(comments.trim());
				comments = "";
				commentsPrinted = true;
			}
			else if (line.contains("import")){
				Pattern patt = Pattern.compile("^import (.*)");
				Matcher m = patt.matcher(line);
				String text = "";
				String[] lineArray = new String[100];
				lineArray = line.split(" ");
		    	 
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
		         commentsPrinted = true; // take it as no more comments after import statements
		         packages = true;
			} else if (line.contains("*") && !commentsPrinted){
				comments += ' ' + line.replace("*", "").trim();
			}
			index = 0;
		}
		if (!commentsPrinted && !comments.trim().equals(""))
			pwStream.print(comments.trim());
		
		if (!textLib.equals("") && description == true) {
			pwStream.println("###" + textLib);
		}
		
		if (description && !packages){
			pwStream.println();
		}
		
		br.close();
	}
	
	/**
	 * Remove characters by position
	 * @param s
	 * @param pos
	 * @return trimmed characters
	 */
	public static String removeCharAt(String s, int pos) {
	      return s.substring(0, pos) + s.substring(pos + 1);
	   }

}
