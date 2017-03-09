package com.example.myproject;

import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class DeleteData extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DatastoreService datastore = 
                DatastoreServiceFactory.getDatastoreService();

		Query mydeleteq = new Query();
		PreparedQuery pq = datastore.prepare(mydeleteq);
		for (Entity result : pq.asIterable()) {
			//if (result.getKey().toString().contains(" vs "))
				datastore.delete(result.getKey());      
		}   
		
		resp.setContentType("text/plain");
		resp.getWriter().println("SUCCESSFULLY DELETED!");
	}
}
