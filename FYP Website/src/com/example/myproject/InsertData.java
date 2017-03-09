package com.example.myproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class InsertData extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = "/WEB-INF/Dataset/Package Level/similarPackage.txt";
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ServletContext context = getServletContext();
		
		InputStream is = context.getResourceAsStream(filename);
		
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", sourcePackage = "", entityKind = "";
			String[] lineArray = null;
			
			int key = 1;	// Change this each time when inserting data
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("###");
				if (lineArray[0].contains(" vs ")){
					entityKind = lineArray[0];
					System.out.println(lineArray[0]);
				} else if (lineArray.length > 1 && !lineArray[lineArray.length - 1].contains("0")){
					sourcePackage = lineArray[0];
				} else if (lineArray.length > 1){
					Entity similarPair = new Entity(entityKind, key++);
					similarPair.setProperty("SourcePackage", sourcePackage);
					similarPair.setProperty("TargetPackage", lineArray[0].toString());
					similarPair.setProperty("TargetDescription", lineArray[1].toString());
					similarPair.setProperty("CombinedSimilarity", Double.parseDouble(lineArray[4].toString()));
					ds.put(similarPair);
				}
			}
			response.setContentType("text/plain");
			response.getWriter().println("SUCCESS!");
		}
	}
}
