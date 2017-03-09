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
    java.io.InputStreamReader,
    java.io.InputStream,
    java.io.BufferedReader,
    java.util.HashMap,
    java.util.Map,
    java.util.Comparator,
    java.util.TreeMap,
    com.google.appengine.api.datastore.Text"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Queried Data</title>
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
	
	/*
	$(document).ready(function() {
		$('table.display').DataTable( {
	        "order": [[ 2, "desc" ]]
	    } );
	} );*/
	</script>
</head>
<body>
	<% 	class ValueComparator implements Comparator<String> {
	    Map<String, Integer> base;

	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with
	    // equals.
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
	
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	
		String pkgMapFile = "/WEB-INF/Dataset/Package Level/packageMappings.txt"; 
		ServletContext contextPkgServlet = getServletContext();
		InputStream isPkg = contextPkgServlet.getResourceAsStream(pkgMapFile);
		
		HashMap<String, String> mapLink = new HashMap<String, String>();
		
		if (isPkg != null) {
			InputStreamReader isr = new InputStreamReader(isPkg);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", entityKind = "";
			String[] lineArray = null;
			
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("###");
				if (lineArray.length > 3){
		    		String pkg = lineArray[1];
		    		String link = lineArray[3];
		    		
					mapLink.put(pkg, link);
				}
			}
		}
	
		String entityKind = request.getParameter("id").toString();    	
    	
    	String filename = "/WEB-INF/Dataset/Package Level/" + entityKind + ".txt"; 
		ServletContext context = getServletContext();
		InputStream is = context.getResourceAsStream(filename);
		
		HashMap<String, String> mapPkgContext = new HashMap<String, String>();
		Query qContext = new Query(entityKind.split(" vs ")[0] + " (Context)");
		PreparedQuery pqContext = ds.prepare(qContext);
		
		for (Entity u2: pqContext.asIterable()){
			String sourcePkg = ((Text)u2.getProperty("SourcePackage")).getValue();
			String contextPkg = ((Text)u2.getProperty("ContextPackage")).getValue();
			mapPkgContext.put(sourcePkg, contextPkg);
		}
		
		ArrayList<String> AL = null;
		
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", sourcePackage = "";
			String[] lineArray = null;
			
			while ((line = reader.readLine()) != null) {
				boolean hasContext = false;
				lineArray = line.split("###");
				if (AL != null && lineArray.length == 5)
	    			AL.add(lineArray[0] + "###" + lineArray[1] + "###" + lineArray[4]);
				else if (lineArray.length == 4){
					sourcePackage = lineArray[0];
					AL = new ArrayList<String>();
		%>
					<h2 style="font-family: 'Open Sans', sans-serif; font-style: italic; font-weight:bold" align="center"><%=sourcePackage%></h2>
			
   		<div class="container">
		<section>
		<table id = "data_table" class="display" cellspacing="0" width="100%">	
		<%			
					if (mapPkgContext.containsKey(sourcePackage)){
		%>	
		<strong style="background-color:yellow">Find the packages you may need</strong>&nbsp;<a href = "SearchContextData.jsp?id=<%=sourcePackage%>">here</a> <br><br>
		<% } %>	
   		<thead>
		    <tr>
		    <th scope="col">Target Package</th>
		    <th scope="col" align="center">Package Description</th>
		    <th scope="col" align="center">Similarity</th>
		    </tr>
		</thead>
		<tbody>
		<%	} else {
			Query qTrack = new Query("ConsolidateTrackings").addFilter("SourcePackage", FilterOperator.EQUAL, sourcePackage.toLowerCase()).addFilter("Context", FilterOperator.EQUAL, "No");
			PreparedQuery pqTrack = ds.prepare(qTrack);
			
			HashMap<String, Integer> packageTrackings = new HashMap<String, Integer>();
			HashMap<String, Integer> fullPackage = new HashMap<String, Integer>();
			ValueComparator bvc = new ValueComparator(fullPackage);
	        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
			
			for (Entity u3: pqTrack.asIterable()){
				String targetPackage = u3.getProperty("TargetPackage").toString();
				int count = Integer.parseInt(u3.getProperty("Count").toString());
				packageTrackings.put(targetPackage, count);
			}
			
			boolean noTrackings = packageTrackings.isEmpty();
			
			String targetPackage = "", targetDescription = "";
	    	Double combinedSimilarity = 0.0;
			
			if (noTrackings){
				for (String eachEntry: AL){
		    		targetPackage = eachEntry.split("###")[0];
		    		targetDescription = eachEntry.split("###")[1];
		    		combinedSimilarity = Double.parseDouble(eachEntry.split("###")[2]);
			%>
				
			<tr>
			<td><a href = "Trackings.jsp?id=<%=mapLink.get(targetPackage)%>&sp=<%=sourcePackage%>&tp=<%=targetPackage%>&context=No" target="_blank"><%=targetPackage%></a></td>
			<td><%=targetDescription%></td>
			<td align="center"><%=combinedSimilarity%></td>
			</tr>
			<% } %>
			</tbody>
			</table>
			</section>
			</div>
				<%
				
			} else {
			for(Map.Entry<String, Integer> entry: packageTrackings.entrySet()){
				for (String eachEntry: AL){
					if (eachEntry.split("###")[0].equals(entry.getKey())){
						fullPackage.put(eachEntry, entry.getValue());
					} else if (!fullPackage.containsKey(eachEntry)){
						fullPackage.put(eachEntry, 0);
					}
				}
			}
			sorted_map.putAll(fullPackage);
		
    	for(Map.Entry<String, Integer> entry : sorted_map.entrySet()) {
    		targetPackage = entry.getKey().split("###")[0];
    		targetDescription = entry.getKey().split("###")[1];
    		combinedSimilarity = Double.parseDouble(entry.getKey().split("###")[2]);
	%>
		
	<tr>
	<td><a href = "Trackings.jsp?id=<%=mapLink.get(targetPackage)%>&sp=<%=sourcePackage%>&tp=<%=targetPackage%>&context=No" target="_blank"><%=targetPackage%></a></td>
	<td><%=targetDescription%></td>
	<td align="center"><%=combinedSimilarity%></td>
	</tr>
	<% }} %>
	</tbody>
	</table>
	</section>
	</div>
	<% }}} %>
</body>
</html>