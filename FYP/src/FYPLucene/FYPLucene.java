/* This program takes in input files, create and search the indexes for target APIs */
package FYPLucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class FYPLucene {
	   static String queryFilePath = "/QueryAPI (preprocessed)";
	   static String filePath = "/FYP\\";
	   static String inputFilePath = "/queryAPI.txt";
	   static String dataDir = "";
	   String indexDir = filePath + "Index";
	   Indexer indexer;
	   Searcher searcher;

	   public static void main(String[] args) throws IOException {
		  FYPLucene tester;
		  
		  FileInputStream similarLibFis = new FileInputStream(inputFilePath);
		  BufferedReader similarLibBr = new BufferedReader(new InputStreamReader(similarLibFis));
		  
		  String similarLibLine = null;
		  ArrayList<String> AL = new ArrayList<String>();
		  while ((similarLibLine = similarLibBr.readLine()) != null){
			  AL.add(similarLibLine);
		  }
		  
		  try {
			  for (String eachSimilarLib: AL){
				 dataDir = filePath + eachSimilarLib.split(" vs ")[1];	// index the target library
				 tester = new FYPLucene();
		         tester.createIndex();
				 FileInputStream fis = new FileInputStream(queryFilePath + "\\" + eachSimilarLib.split(" vs ")[0] + ".txt"); // query the source library
				 BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				 String line = null;
		         
				 while ((line = br.readLine()) != null){
					 if (line.split("###").length > 1)
						 tester.search(line.trim(), eachSimilarLib);
				 }
				 br.close();
			  }
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (ParseException e) {
	         e.printStackTrace();
	      }
	   }

	   /**
	    * Create indexes for the input file
	    * @throws IOException
	    */
	   private void createIndex() throws IOException{
	      indexer = new Indexer(indexDir);
	      int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
	      long endTime = System.currentTimeMillis();
	      indexer.close();
	      System.out.println(numIndexed+" File indexed, time taken: "
	         +(endTime-startTime)+" ms");		
	   }

	   /**
	    * Search the index file
	    * @param searchQuery
	    * @param dataFolder
	    * @throws IOException
	    * @throws ParseException
	    */
	   private void search(String searchQuery, String dataFolder) throws IOException, ParseException{
	      searcher = new Searcher(indexDir);
	      long startTime = System.currentTimeMillis();
	      TopDocs hits = searcher.search(searchQuery.split("###")[1]);
	      long endTime = System.currentTimeMillis();
	   
	      FileWriter fwStream = new FileWriter(queryFilePath + "\\Results\\" + dataFolder + ".txt", true);
		  BufferedWriter bwStream = new BufferedWriter(fwStream);
		  PrintWriter pwStream = new PrintWriter(bwStream);
	      
		  if (hits.scoreDocs.length > 0)
			  pwStream.println(searchQuery + "###" + dataFolder.split(" vs ")[0] + "###" + dataFolder.split(" vs ")[1]);
		  	
	      System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime));
	      for(ScoreDoc scoreDoc : hits.scoreDocs) {
	         Document doc = searcher.getDocument(scoreDoc);
	         pwStream.println(doc.get(LuceneConstants.FILE_PATH).split("\\\\")[9].split("\\.txt")[0] + "###" + scoreDoc.score);
	      }
	      pwStream.println();
	      pwStream.close();
	      searcher.close();
	   }
}
