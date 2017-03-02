/* This program crawls each library's package descriptions in the list of javadoc file */
package Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Crawler {
		static String inputFilePath = "/Javadoc.txt"; 
		static String outputFilePath = "/Package descriptions"; 
		public static void main(String[] args) throws IOException {
			ReadMainLinks();
	    }
	 	
		/**
		 * Read all the links in the summary page of the library javadoc
		 * @throws IOException
		 */
		public static void ReadMainLinks () throws IOException{
			FileInputStream fis = new FileInputStream(inputFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			
			String line = null;
			String[] lineArray = new String[5];
			
			ArrayList<String> AL = new ArrayList<String>();
			int count = 0;
			while ((line = br.readLine()) != null){
				lineArray = line.split("\t");
				AL = CrawlerLinks(lineArray[1]);
				if (lineArray.length == 2){		// Crawl the standard version of javadoc (i.e. opennlp)
					for (int i = 0; i < AL.size() - 1; i++){
			        	if (AL.get(i).replace("http://", "").replace("https://", "").split("/").length >= 3){
							CrawlerPackageNormal(AL.get(i), lineArray[0]);
			        	}
			        }
				} else if (lineArray.length == 3){	// Crawl the special version of javadoc (i.e. bean-io)
					for (int i = 0; i < AL.size() - 1; i++){
			        	if (AL.get(i).replace("http://", "").replace("https://", "").split("/").length >= 4){
							CrawlerPackageSpecial(AL.get(i), lineArray[0]);
			        	}
			        }
				} else if (lineArray.length == 4){	// Crawl the standard version of javadoc with no summary page (i.e. org.json)
					if (lineArray[1].replace("http://", "").replace("https://", "").split("/").length >= 5){
						CrawlerPackageNormal(lineArray[1], lineArray[0]);
					}
				} else if (lineArray.length == 5){	// Crawl the very old version of javadoc with no summary page (i.e. sitemesh)
					for (int i = 0; i < AL.size() - 1; i++){
			        	if (AL.get(i).replace("http://", "").replace("https://", "").split("/").length >= 5){
							CrawlerPackageVeryOldLibrary(AL.get(i), lineArray[0]);
			        	}
			        }
				}
				count++;
			}
		}
		/**
		 * Crawls library with javadoc that are updated
		 * @param url
		 * @param libraryName
		 * @throws IOException
		 */
	 	public static void CrawlerPackageNormal(String url, String libraryName) throws IOException{
	 		
	 		String newUrl = "";
	 		if (url.startsWith("http://") || url.startsWith("https://"))
	 			newUrl = url;
	 		else return;
	 		
	 		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + libraryName + ".csv", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
	        Document doc = Jsoup.connect(newUrl).timeout(0).ignoreContentType(true).ignoreHttpErrors(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	        
	        Elements packageName = doc.select(".title");
	        Elements className = doc.select("td.colFirst");
	        Elements description = doc.select("td.colLast");
	        
	        int index = 0;
	        int size = description.size();
	        
	        size = (className.size() < description.size()) ? className.size() : description.size();
	       
	        while (index < size){
	        	if (packageName.size() > 0 && packageName.get(0).text().contains("Package")){
	        		pwStream.println(packageName.get(0).text().replace("Package ", "") + "." + className.get(index).text().replaceAll("<.*?>", "") + "###" + description.get(index).text() + "###" + className.get(index).getElementsByTag("a").attr("abs:href").toString());
	        	}
	        	index++;
	        } 
	        
	        pwStream.close();
	 	}
	 
	 	/**
	 	 * Crawls library with javadoc that are not updated
	 	 * @param url
	 	 * @param libraryName
	 	 * @throws IOException
	 	 */
	 	public static void CrawlerPackageSpecial(String url, String libraryName) throws IOException{
	 		
	 		String newUrl = "";
	 		if (url.startsWith("http://") || url.startsWith("https://"))
	 			newUrl = url;
	 		else return;
	 		
	 		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + libraryName + ".csv", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
	        Document doc = Jsoup.connect(newUrl).timeout(0).ignoreContentType(true).ignoreHttpErrors(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	        
	        
	        Elements packageName = doc.select("h2");
	        Elements description = doc.select("td");
	        //Element table = doc.select("table").get(2); //select the first table.
	        //Elements rows = table.select("tr");

	        String result, packageUrl = "";
	        int index = 0;
	        while (index < doc.select("table").size()){
	        	Element table = doc.select("table").get(index); 
		        Elements rows = table.select("tr");
		        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
		        	result = "";
		            Element row = rows.get(i);
		            Elements cols = row.select("td");
		            url = "";
		            for (int j = 0; j < cols.size(); j++){
		            	if (j == cols.size() - 1)
		            		result += cols.get(j).text() + "###" + packageUrl;
		            	else if (packageName.size() > 0) {
		            		result += packageName.get(0).text().replace("Package ", "").trim() + "." + cols.get(j).text().replaceAll("<.*?>", "").trim() + "###";
		            		packageUrl = cols.get(j).getElementsByTag("a").attr("abs:href");
		            	}
		            }
		            if (!result.equals(""))
		            	pwStream.println(result);
		        }
		        index++;
	        }
	        pwStream.close();
	 	}
	 	
	 	/**
	 	 * Crawls library with javadoc that are not updated for a long time
	 	 * @param url
	 	 * @param libraryName
	 	 * @throws IOException
	 	 */
	 	public static void CrawlerPackageVeryOldLibrary(String url, String libraryName) throws IOException{
	 		
	 		String newUrl = "";
	 		if (url.startsWith("http://") || url.startsWith("https://"))
	 			newUrl = url;
	 		else return;
	 		
	 		FileWriter fwStream = new FileWriter(outputFilePath + "\\" + libraryName + ".csv", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
	        Document doc = Jsoup.connect(newUrl).timeout(0).ignoreContentType(true).ignoreHttpErrors(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	        
	        
	        Elements packageName = doc.select("h2");
	        Elements description = doc.select("td");
	        //Element table = doc.select("table").get(2); //select the first table.
	        //Elements rows = table.select("tr");

	        String result, packageUrl = "";
	        int index = 0;
	        while (index < doc.select("table").size()){
	        	Element table = doc.select("table").get(index); 
		        Elements rows = table.select("tr");
		        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
		        	result = "";
		            Element row = rows.get(i);
		            Elements cols = row.select("td");
		            for (int j = 0; j < cols.size(); j++){
		            	if (j != 0 && j < 2)
		            		result += cols.get(j).text().trim() + "###" + packageUrl;
		            	else if (packageName.size() > 0 && j == 0){
		            		result += packageName.get(0).text().replace("Package ", "").trim() + "." + cols.get(j).text().replaceAll("<.*?>", "").replaceAll("\u00a0+", "").trim() + "###";
		            		packageUrl = cols.get(j).getElementsByTag("a").attr("abs:href").toString();
		            	}
		            }
		            if (result.split("###").length > 2)
		            	pwStream.println(result);
		        }
		        index++;
	        }
	        pwStream.close();
	 	}
	 	
	 	
	 	/**
	 	 * Crawls all the links given a url
	 	 * @param url
	 	 * @return	arraylist of urls
	 	 * @throws IOException
	 	 */
	 	public static ArrayList<String> CrawlerLinks(String url) throws IOException{
	 		ArrayList<String> linkArrayList = new ArrayList<String>();
	 		Document doc = Jsoup.connect(url).timeout(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	 		Elements links = doc.select("a[href]");
	 		
	 		for (Element link : links) {
	            linkArrayList.add(link.attr("abs:href").toString());
	        }
	 		return linkArrayList;
	 	}
}
