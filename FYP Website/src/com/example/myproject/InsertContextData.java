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
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class InsertContextData extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = "/WEB-INF/Dataset/Package Level/packageContext.txt";
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ServletContext context = getServletContext();
		
		InputStream is = context.getResourceAsStream(filename);
		int key = 1;
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", entityKind = "";
			String[] lineArray = null;
			
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("###");
				
				if (lineArray.length > 1){
					Entity similarPair = new Entity(entityKind, key++);
					Text sourcePackage = new Text(lineArray[0]);
					Text ContextPackage = new Text(lineArray[1]);
					similarPair.setProperty("SourcePackage", sourcePackage);					
					similarPair.setProperty("ContextPackage", ContextPackage);
					ds.put(similarPair);
				} else {
					entityKind = line.trim() + " (Context)";
					key = 1;
				}
			}
			response.setContentType("text/plain");
			response.getWriter().println("SUCCESS!");
		}
	}
}
