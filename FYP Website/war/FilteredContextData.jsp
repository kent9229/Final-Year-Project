<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.google.appengine.api.datastore.DatastoreService,
    com.google.appengine.api.datastore.DatastoreServiceFactory,
    com.google.appengine.api.datastore.Query,
    com.google.appengine.api.datastore.PreparedQuery,
    com.google.appengine.api.datastore.Entity,
    com.google.appengine.api.datastore.Query.FilterOperator,
    com.google.appengine.api.datastore.FetchOptions,
    com.google.appengine.api.datastore.QueryResultList,
    java.util.ArrayList,
    java.util.HashMap,
    java.util.Map,
    com.google.appengine.api.datastore.Text,
    java.io.InputStreamReader,
    java.io.InputStream,
    java.io.BufferedReader;"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Queried Context Data</title>
<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<style type="text/css">

	div.dataTables_wrapper {
		margin-bottom: 3em;
	}

	</style>
	<script type="text/javascript" language="javascript" src="js/jquery-dt.js"></script>
	<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript">

	$(document).ready(function() {
		$('table.display').DataTable( {
			"aaSorting": []
	    } );
	} );
	</script>
</head>
<body>
	<% 	
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	String library = request.getParameter("id").toString();    	
    	
    	String filename = "/WEB-INF/Dataset/Package Level/packageMappings.txt"; 
		ServletContext context = getServletContext();
		InputStream is = context.getResourceAsStream(filename);
    	
		HashMap<String, String> map = new HashMap<String, String>();
    	HashMap<String, String> mapPkg = new HashMap<String, String>();
    	HashMap<String, String> mapPkgContext = new HashMap<String, String>();
		
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", entityKind = "";
			String[] lineArray = null;
			
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("###");
				String lib = lineArray[0];
	    		String pkg = lineArray[1];
	    		String desc = "";
	    		if (lineArray.length > 2) desc = lineArray[2];
				
				if (lib.equals(library)){
					map.put(lib + "###" + pkg, desc);    	
		    		mapPkg.put(pkg, desc);
				}
			}
		}
		
    	Query qContext = new Query(library + " (Context)");
    	PreparedQuery pqContext = ds.prepare(qContext);
    	
    	for (Entity u2: pqContext.asIterable()){
    		String sourcePkg = ((Text)u2.getProperty("SourcePackage")).getValue();
    		String contextPkg = ((Text)u2.getProperty("ContextPackage")).getValue();
    		mapPkgContext.put(sourcePkg, contextPkg);
    	}
    	
    	for(Map.Entry<String, String> entry: map.entrySet()){
    		String sourceLibrary = entry.getKey().split("###")[0];
    		String sourcePackage = entry.getKey().split("###")[1];
    		if (sourceLibrary.equals(library) && mapPkgContext.containsKey(sourcePackage)){
    %>
   		<h2 style="font-family: 'Open Sans', sans-serif; font-style: italic; font-weight:bold" align="center"><strong><%=sourcePackage%></strong></h2>
   		<div class="container">
		<section>
		<table id = "" class="display" cellspacing="0" width="100%">		
   		<thead>
		    <tr>
		    <th scope="col">Package Name</th>
		    <th scope="col">Package description</th>
		    </tr>
		</thead>
		<tbody>
    <%    	
    			String contextPackage = mapPkgContext.get(sourcePackage);
		   		String[] contextPkgArray = contextPackage.split(", ");
		   		for (String eachContextPkg: contextPkgArray){
					String eachContextDescription = mapPkg.get(eachContextPkg);
					if (eachContextDescription == null) eachContextDescription = "";
	%>
	
	<tr>
	<td><%=eachContextPkg%></td>
	<td><%=eachContextDescription%></td>
	</tr>
	<% } %>
	</tbody>
	</table>
	</section>
	</div>
	<% }} %>
</body>
</html>