<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.google.appengine.api.datastore.DatastoreService,
    com.google.appengine.api.datastore.DatastoreServiceFactory,
    com.google.appengine.api.datastore.Entity,
    com.google.appengine.api.datastore.Query,
    com.google.appengine.api.datastore.PreparedQuery,
    com.google.appengine.api.datastore.Query.FilterOperator,
    java.util.Date,
    java.text.DateFormat,
    java.text.SimpleDateFormat,
    com.google.appengine.api.datastore.Key"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Redirecting</title>
</head>
<body onload="redirect()">
	<%
	String url = request.getParameter("id").toString();
	String sourcePackage = request.getParameter("sp").toString();
	String targetPackage = request.getParameter("tp").toString();
	String context = request.getParameter("context").toString();
	
    String ipAddress = request.getHeader("HTTP_CLIENT_IP");
	if (ipAddress == null) {
		 ipAddress = request.getRemoteAddr();
	}
	
	DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    Date dateobj = new Date();
    String dateTime = df.format(dateobj).toString();
	
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	ServletContext contextSevrv = getServletContext();
	
	// For Individual Trackings
	Query queryIT = new Query("IndividualTrackings");
	PreparedQuery pqIT = ds.prepare(queryIT);
	
	int ITKey = 0;
	for(Entity entity : pqIT.asIterable()) {
		ITKey++;
	}
	
	Entity individualTrackings = new Entity("IndividualTrackings", ITKey + 1);
	individualTrackings.setProperty("IPAddress", ipAddress);
	individualTrackings.setProperty("DateTime", dateTime);
	individualTrackings.setProperty("SourcePackage", sourcePackage.toLowerCase());
	individualTrackings.setProperty("TargetPackage", targetPackage);
	if (context.equals("Yes")) individualTrackings.setProperty("Context", "Yes");
	else individualTrackings.setProperty("Context", "No");
	ds.put(individualTrackings);
	
	// For consolidated trackings
	Query queryCT = new Query("ConsolidateTrackings");
	PreparedQuery pqCT = ds.prepare(queryCT);
	
	Key CTKey = null; int newCTKey = 0;
	for(Entity entity : pqCT.asIterable()) {
		if (entity.getProperty("SourcePackage").toString().equals(sourcePackage.toLowerCase()) &&
				entity.getProperty("TargetPackage").toString().equals(targetPackage) && 
				entity.getProperty("Context").toString().equals(context)){
			CTKey = entity.getKey();
		}
		newCTKey++;
	}
	
	if (CTKey == null){
		Entity consolidateTrackings = new Entity("ConsolidateTrackings", newCTKey + 1);
		consolidateTrackings.setProperty("SourcePackage", sourcePackage.toLowerCase());
		consolidateTrackings.setProperty("TargetPackage", targetPackage);
		consolidateTrackings.setProperty("Count", 1);
		if (context.equals("Yes")) consolidateTrackings.setProperty("Context", "Yes");
		else consolidateTrackings.setProperty("Context", "No");
		ds.put(consolidateTrackings);
	} else {
		Entity existingRecord = ds.get(CTKey);
		int existingCount = Integer.parseInt(existingRecord.getProperty("Count").toString());
		Entity consolidateTrackings = new Entity("ConsolidateTrackings", CTKey.getId());
		consolidateTrackings.setProperty("SourcePackage", sourcePackage.toLowerCase());
		consolidateTrackings.setProperty("TargetPackage", targetPackage);
		consolidateTrackings.setProperty("Count", existingCount + 1);
		consolidateTrackings.setProperty("Context", context);
		ds.put(consolidateTrackings);
	}

   	//response.sendRedirect(url);
	%>
	<script language=javascript>
	function redirect(){
	  window.location = "<%=url%>";
	}
	</script>
</body>
</html>