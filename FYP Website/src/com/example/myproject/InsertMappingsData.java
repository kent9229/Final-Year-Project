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
public class InsertMappingsData extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = "/WEB-INF/Dataset/Package Level/packageMappings.txt";
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ServletContext context = getServletContext();
		
		InputStream is = context.getResourceAsStream(filename);
		
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", entityKind = "packageMappings";
			String[] lineArray = null;
			
			int key = 69001;	// Change this each time when inserting data
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("###");
				String description = "";
				if (lineArray.length > 1){
					Entity similarPair = new Entity(entityKind, key++);
					similarPair.setProperty("Library", lineArray[0].toString());					
					similarPair.setProperty("Package", lineArray[1].toString());
					if (lineArray.length != 2) description = lineArray[2].trim().toString();  
					similarPair.setProperty("Description", description);
					ds.put(similarPair);
				}
			}
			response.setContentType("text/plain");
			response.getWriter().println("SUCCESS!");
		}
	}
}
