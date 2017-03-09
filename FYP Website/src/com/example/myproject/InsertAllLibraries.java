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
public class InsertAllLibraries extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = "/WEB-INF/Dataset/AllLibraries.txt";
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ServletContext context = getServletContext();
		
		InputStream is = context.getResourceAsStream(filename);
		
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", entityKind = "Libraries";
			
			int key = 1;	
			while ((line = reader.readLine()) != null) {
				Entity similarPair = new Entity(entityKind, key++);
				similarPair.setProperty("Library", line.trim());
				ds.put(similarPair);
			}
			response.setContentType("text/plain");
			response.getWriter().println("SUCCESS!");
		}
	}
}
