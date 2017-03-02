/*This program crawls the APIs' descriptions of each class within the library */
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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

public class CrawlerMethod {
		static String inputFilePath = "/Javadoc.txt"; 	
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
			
			ArrayList<String> ALMain = new ArrayList<String>();
			ArrayList<String> ALClass = new ArrayList<String>();
			int count = 0;
			while ((line = br.readLine()) != null){
				try {
					lineArray = line.split("\t");
					ALMain = CrawlerLinks(lineArray[1]);
					int index = 0;
					if (lineArray.length == 2){	// Crawl the standard version of javadoc (i.e. opennlp)
						for (int i = 0; i < ALMain.size() - 1; i++){
							index = 0;
							if (ALMain.get(i).replace("http://", "").replace("https://", "").split("/").length >= 3){
								Document doc = Jsoup.connect(ALMain.get(i)).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
						 		Elements links = doc.select("td.colFirst");
						 		while (index < links.size()){
						 			CrawlerMethodNormal(links.get(index).getElementsByTag("a").attr("abs:href"), lineArray[0]);
						 			index++;
						 		}
							}
				        }
					} else if (lineArray.length == 3){	// Crawl the special version of javadoc (i.e. bean-io)
						for (int i = 0; i < ALMain.size() - 1; i++){
							if (ALMain.get(i).replace("http://", "").replace("https://", "").split("/").length >= 4){
								Document doc = Jsoup.connect(ALMain.get(i)).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
								index = 0;
							
								while (index < doc.select("table").size()){
						        	Element table = doc.select("table").get(index); 
							        Elements rows = table.select("tr");
							        for (int j = 0; j < rows.size(); j++) {
							            Element row = rows.get(j);
							            Elements cols = row.select("td");
							            for (int k = 0; k < cols.size(); k++){
							            	if (cols.get(k).getElementsByTag("a").attr("abs:href").replace("http://", "").replace("https://", "").split("/").length >= 5)
							            		System.out.println(cols.get(k).getElementsByTag("a").attr("abs:href"));
							            		//CrawlerMethodSpecial(cols.get(k).getElementsByTag("a").attr("abs:href"), lineArray[0]);
							            }
							        }
							        index++;
						        }
							}
				        }
					} else if (lineArray.length == 4){	// Crawl the standard version of javadoc with no summary page (i.e. org.json)
						if (lineArray[1].replace("http://", "").replace("https://", "").split("/").length >= 3){
							Document doc = Jsoup.connect(lineArray[1]).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
					 		Elements links = doc.select("td.colFirst");
					 		while (index < links.size()){
					 			CrawlerMethodNormal(links.get(index).getElementsByTag("a").attr("abs:href"), lineArray[0]);
					 			index++;
					 		}
						}
					}else if (lineArray.length == 5){	// Crawl the very old version of javadoc with no summary page (i.e. sitemesh)
						for (int i = 0; i < ALMain.size() - 1; i++){
							if (ALMain.get(i).replace("http://", "").replace("https://", "").split("/").length >= 5){
								Document doc = Jsoup.connect(ALMain.get(i)).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
								index = 0;
							
								while (index < doc.select("table").size()){
						        	Element table = doc.select("table").get(index); 
							        Elements rows = table.select("tr");
							        for (int j = 0; j < rows.size(); j++) {
							            Element row = rows.get(j);
							            Elements cols = row.select("td");
							            for (int k = 0; k < cols.size(); k++){
							            	if (cols.get(k).getElementsByTag("a").attr("abs:href").replace("http://", "").replace("https://", "").split("/").length >= 5)
							            		//System.out.println(cols.get(k).getElementsByTag("a").attr("abs:href"));
							            		CrawlerMethodVeryOldLibrary(cols.get(k).getElementsByTag("a").attr("abs:href"), lineArray[0]);
							            }
							        }
							        index++;
						        }
							}
				        }
					}
					count++;
				} catch (SocketTimeoutException e){
					e.printStackTrace();
				}
			}
		}
		/**
		 * Crawls library with javadoc that are updated
		 * @param url
		 * @param libraryName
		 * @throws IOException
		 */
	 	public static void CrawlerMethodNormal(String url, String libraryName) throws IOException, SocketTimeoutException{
	 		String newUrl = "";
	 		if (url.startsWith("http://") || url.startsWith("https://"))
	 			newUrl = url;
	 		else return;
	 		//System.out.println("New url: " + newUrl);
	 		FileWriter fwStream = new FileWriter("C:\\Users\\Kent Ong\\Dropbox\\CSC\\FYP\\Data\\Crawler\\API descriptions\\" + libraryName + ".csv", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
	        Document doc = Jsoup.connect(newUrl).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	        
	        Elements packageName = doc.select(".subTitle");
	        Elements className = doc.select(".title");
	        Elements description = doc.select("td.colLast");
	        
	        int index = 0;
	        int size = description.size();
	        
	        while (index < size){
	        	try {
	        		//System.out.println(description.get(index).text().trim());
		        	if (packageName.size() > 0 && className.size() > 0 && (className.get(0).text().contains("Class") || className.get(0).text().contains("Interface"))){
		        		String desc = description.get(index).text().trim();
		        		pwStream.println(packageName.get(0).text() + "." + className.get(0).text().replace("Package ", "").replace("Class ", "").replace("Interface ", "").replaceAll("<.*?>", "") + "." + description.get(index).text().replaceAll("<.*?>", "").split("[(.*?)]")[0].split(" ")[0].trim() + "###" + desc.substring(desc.indexOf(")")+1, desc.length()).trim() + "###" + description.get(index).getElementsByTag("a").attr("abs:href").replace(" ", ""));
		        		//print("* %s.%s - %s", MethodName.get(0).text().replace("Method ", ""), className.get(index).text().replaceAll("<.*?>", ""), description.get(index).text());
		        	}
		        	index++;
	        	} catch (Exception e){
	        		e.printStackTrace();
	        	}
	        } 
	        
	        pwStream.close();
	 	}
	 
	 	/**
	 	 * Crawls library with javadoc that are not updated
	 	 * @param url
	 	 * @param libraryName
	 	 * @throws IOException
	 	 */
	 	public static void CrawlerMethodSpecial(String url, String libraryName) throws IOException, SocketTimeoutException{
	 		String newUrl = "";
	 		if (url.startsWith("http://") || url.startsWith("https://"))
	 			newUrl = url;
	 		else return;
	 		FileWriter fwStream = new FileWriter("C:\\Users\\Kent Ong\\Dropbox\\CSC\\FYP\\Data\\Crawler\\Package Level\\Package descriptions\\" + libraryName + ".csv", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
	        Document doc = Jsoup.connect(newUrl).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	        
	        Elements packageName = doc.select("h2");
	        Elements description = doc.select("td");
	        //Element table = doc.select("table").get(2); //select the first table.
	        //Elements rows = table.select("tr");

	        String result;
	        String[] descArray = new String[100];
	        int index = 0;
	        
	        while (index < doc.select("table").size()){
	        	try {
		        	Element table = doc.select("table").get(index); 
			        Elements rows = table.select("tr");
			        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
			        	result = "";
			            Element row = rows.get(i);
			            Elements cols = row.select("td");
			            Elements code = cols.select("code");
			            int size = code.size();
			            size = (cols.size() < code.size()) ? cols.size() : code.size();
			            for (int j = 0; j < size; j++){
			            	if (packageName.size() > 0 && code.get(j).text().contains("(")){
			            		descArray = cols.get(j).text().replaceAll("\u00a0+", "@@@").split("@@@");
				            	result = packageName.get(0).text().replace(" Package ", ".").replace(" Interface ", ".").replace("Class ", "").replace(" ", ".").replaceAll("<.*?>", "") + "." + code.get(j).text().replaceAll("\\(.+\\)", "").replace("()", "").replaceAll("<.*?>", "") + "###" + descArray[descArray.length - 1].substring(descArray[descArray.length-1].indexOf(")")+1, descArray[descArray.length-1].length()).trim() + "###" + cols.get(j).getElementsByTag("a").attr("abs:href").replace(" ", "");
			            	}
			            }
			            if (result != "")
			            	pwStream.println(result);
			        }
			        index++;
	        	} catch (Exception e){
	        		e.printStackTrace();
	        	}
	        }
	        pwStream.println(url);
	        pwStream.close();
	 	}
	 	
	 	/**
	 	 * Crawls library with javadoc that are not updated for a long time
	 	 * @param url
	 	 * @param libraryName
	 	 * @throws IOException
	 	 */
	 	public static void CrawlerMethodVeryOldLibrary(String url, String libraryName) throws IOException, SocketTimeoutException{
	 		String newUrl = "";
	 		if (url.startsWith("http://") || url.startsWith("https://"))
	 			newUrl = url;
	 		else return;
	 		FileWriter fwStream = new FileWriter("C:\\Users\\Kent Ong\\Dropbox\\CSC\\FYP\\Data\\Crawler\\API descriptions\\" + libraryName + ".csv", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
	        Document doc = Jsoup.connect(newUrl).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	        
	        Elements packageName = doc.select("h2");
	        Elements description = doc.select("td");
	        //Element table = doc.select("table").get(2); //select the first table.
	        //Elements rows = table.select("tr");

	        String result;
	        String[] descArray = new String[100];
	        int index = 0;
	        while (index < doc.select("table").size()){
	        	try {
		        	Element table = doc.select("table").get(index); 
			        Elements rows = table.select("tr");
			        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
			        	result = "";
			            Element row = rows.get(i);
			            Elements cols = row.select("td");
			            int size = cols.size();
			            for (int j = 0; j < size; j++){
			            	if (packageName.size() > 0 && cols.size() > 1){
				            	String classUrl = cols.get(0).getElementsByTag("a").attr("abs:href");
				            	CrawlerMethodVeryOldLibraryAPI(classUrl, libraryName);
				            	//result = packageName.get(0).text() + "." + cols.get(0).text().replaceAll("\\(.+\\)", "").replace("()", "").replaceAll("<.*?>", "").replaceAll("\u00a0+", "").trim() + "###" + cols.get(1).text();
				            	break;
			            	}
			            }
			            if (result != "")
			            	pwStream.println(result);
			        }
			        index++;
	        	} catch (Exception e){
	        		e.printStackTrace();
	        	}
	        }
	        pwStream.close();
	 	}
	 	
	 	/**
	 	 * Crawls library's API with javadoc that are not updated
	 	 * @param url
	 	 * @param libraryName
	 	 * @throws IOException
	 	 */
	 	public static void CrawlerMethodVeryOldLibraryAPI(String url, String libraryName) throws IOException, SocketTimeoutException{
	 		String newUrl = "";
	 		if (url.startsWith("http://") || url.startsWith("https://"))
	 			newUrl = url;
	 		else return;
	 		FileWriter fwStream = new FileWriter("C:\\Users\\Kent Ong\\Dropbox\\CSC\\FYP\\Data\\Crawler\\API descriptions\\" + libraryName + ".csv", true);
			BufferedWriter bwStream = new BufferedWriter(fwStream);
			PrintWriter pwStream = new PrintWriter(bwStream);
			
	        Document doc = Jsoup.connect(newUrl).timeout(0).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
	        
	        int tableIndex = 2;
	        int rowIndex = 1;
	        
	        Elements packageName = doc.select("#pkg");
	        Elements className = doc.select("#cls");
	        
	        if (packageName.size() == 0)
	        	return;
	        String packageAndClassName = packageName.get(0).text() + "." + className.get(0).text().split(":")[1].trim(); 
	        
	        if (doc.select("table").size() == 0)
	        	return;
	        if (doc.select("table").size() <= 2)
	        	tableIndex = 0;
	        while (tableIndex < doc.select("table").size()){	// check if the first link contains "#"
	        	if (doc.select("table").get(tableIndex).select("tr").size() > 1)
	        		if (doc.select("table").get(tableIndex).select("tr").get(rowIndex).select("a[href]").size() > 0)
	        			if (doc.select("table").get(tableIndex).select("tr").get(rowIndex).select("a[href]").get(0).toString().contains("#"))
	        				if(doc.select("table").get(tableIndex).select("tr").get(0).text().toString().contains(packageAndClassName))
	        					break;
	        	tableIndex++;
	        }
	        
	        if (tableIndex == doc.select("table").size())
	        	return;
	        
	        Element table = doc.select("table").get(tableIndex); //select the table with the method level links
	        Elements rows = table.select("tr");
	        //System.out.println("Total number tables: " + doc.select("table").size());
	        
	        String result;
	        String[] descArray = new String[100];
	        int index = 0;
	        
        	Element row = rows.get(rowIndex);
        	Elements links = row.select("a[href]");
        	//System.out.println(links.size());
        	HashMap<String, String> mapAPI = new HashMap<String, String>();
        	
        	for (Element link : links) {
        		String API = link.toString().split("#")[1];
        		API = API.substring(0, API.indexOf("\""));
        		if (API.contains("("))
        			API = API.substring(0, API.indexOf("("));
        		mapAPI.put(API, link.attr("abs:href").toString());	// Put all the links into a hashmap
        		//System.out.println(API + " => " + link.attr("abs:href").toString());
	        }
        	/*
        	for(Map.Entry<String, String> entry: mapAPI.entrySet()){
    	    	System.out.println(entry.getKey()+ " ===== "+ entry.getValue());
    	    }
        	*/
        	int APIRowIndex = 1;	// row index for API description
        	//System.out.println(doc.select("table").get(tableIndex).select("tr").get(APIRowIndex).select("ul").text());
        	while (tableIndex < doc.select("table").size()){	// To find the correct  table index
        		String API = doc.select("table").get(tableIndex).select("tr").get(APIRowIndex).select("b").text().trim();
        		if (mapAPI.containsKey(API))
        			break;
        		tableIndex++;
        	}
        	
        	while (APIRowIndex < doc.select("table").get(tableIndex).select("tr").size()){
        		String API = doc.select("table").get(tableIndex).select("tr").get(APIRowIndex).select("b").text().trim();
        		String APIdesc = doc.select("table").get(tableIndex).select("tr").get(APIRowIndex).select("ul").text().trim();
				pwStream.println(packageName.get(0).text() + "." + className.get(0).text().split(":")[1].trim() + "." + API + "###" + APIdesc + "###" + mapAPI.get(API));
				APIRowIndex++;
        	}

	        pwStream.close();
	 	}
	 	
	 	/**
	 	 * Crawls all the links given a url
	 	 * @param url
	 	 * @return arraylist of urls
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
