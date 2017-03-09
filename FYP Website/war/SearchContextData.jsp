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
    com.google.appengine.api.datastore.Entities,
    java.util.HashMap,
    java.util.Map,
    java.util.Comparator,
    java.util.TreeMap,
    com.google.appengine.api.datastore.Text,
    java.io.InputStreamReader,
    java.io.InputStream,
    java.io.BufferedReader"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Context Data</title>
<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<style type="text/css">

	div.dataTables_wrapper {
		margin-bottom: 3em;
	}

	</style>
	<script type="text/javascript" language="javascript" src="js/jquery-dt.js"></script>
	<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" class="init">

	$(document).ready(function() {
		$('table.display').DataTable( {
			"aaSorting": []
	    } );
	} );
	</script>
</head>
<body>
<div class="container">
<section>
<table id = "" class="display" cellspacing="0" width="100%">
<thead>	
    <tr>
    <th scope="col">Package Name</th>
    <th scope="col" align="center">Package Description</th>
    </tr>
</thead>
<tbody>
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
		String queryPackage = request.getParameter("id").toString();    	
		
		String filename = "/WEB-INF/Dataset/Package Level/packageMappings.txt"; 
		ServletContext context = getServletContext();
		InputStream is = context.getResourceAsStream(filename);
    	
		HashMap<String, String> map = new HashMap<String, String>();
    	HashMap<String, String> mapPkg = new HashMap<String, String>();
    	HashMap<String, String> mapLib = new HashMap<String, String>();
    	HashMap<String, String> mapLink = new HashMap<String, String>();
    	
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			String line = "", entityKind = "";
			String[] lineArray = null;
			
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("###");
				
				if (lineArray.length > 3){
					String lib = lineArray[0];
		    		String pkg = lineArray[1];
		    		String desc = "";
		    		if (lineArray.length > 2) desc = lineArray[2];
					String link = lineArray[3];
		    		
					mapPkg.put(pkg, desc);
					mapLib.put(pkg, lib);
					mapLink.put(pkg, link);
				}
			}
		}
		
		HashMap<String, String> mapPkgContext = new HashMap<String, String>();
		Query qContext = null;
		if (mapLib.containsKey(queryPackage))
			qContext = new Query(mapLib.get(queryPackage) + " (Context)");
		else return;
		PreparedQuery pqContext = ds.prepare(qContext);
		
		String alrexistPkg = "";
		for (Entity u2: pqContext.asIterable()){
			String sourcePkg = ((Text)u2.getProperty("SourcePackage")).getValue();
			String contextPkg = ((Text)u2.getProperty("ContextPackage")).getValue();
			mapPkgContext.put(sourcePkg, contextPkg);
		}
		
		String contextPackage = "";
		if (mapPkgContext.containsKey(queryPackage))
			contextPackage = mapPkgContext.get(queryPackage);
		else return;
		String[] contextPackageArray = contextPackage.split(", ");
		
		Query qTrack = new Query("ConsolidateTrackings").addFilter("SourcePackage", FilterOperator.EQUAL, queryPackage.toLowerCase()).addFilter("Context", FilterOperator.EQUAL, "Yes");
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
		
		if (noTrackings){
		
		for (String eachContextPackage: contextPackageArray){
			String eachContextDescription = mapPkg.get(eachContextPackage);
			if (eachContextDescription == null) eachContextDescription = "";
	%>
	<tr>
	<td><a href = "Trackings.jsp?id=<%=mapLink.get(eachContextPackage)%>&sp=<%=queryPackage%>&tp=<%=eachContextPackage%>&context=Yes" target="_blank"><%=eachContextPackage%></a></td>
	<td><%=eachContextDescription%></td>
	</tr>
	<% } %>
	</tbody>
    </table>
    </section>
	</div>
	<% } else {
		for(Map.Entry<String, Integer> entry: packageTrackings.entrySet()){
			for (String eachContextPackage: contextPackageArray){
				if (eachContextPackage.equals(entry.getKey())){
					fullPackage.put(eachContextPackage, entry.getValue());
				} else if (!fullPackage.containsKey(eachContextPackage)){
					fullPackage.put(eachContextPackage, 0);
				}
			}
		}
		sorted_map.putAll(fullPackage);
		
		for(Map.Entry<String, Integer> entry : sorted_map.entrySet()) {
			String eachContextDescription = mapPkg.get(entry.getKey());
			if (eachContextDescription == null) eachContextDescription = "";
	%>
	<tr>
	<td><a href = "Trackings.jsp?id=<%=mapLink.get(entry.getKey())%>&sp=<%=queryPackage%>&tp=<%=entry.getKey()%>&context=Yes" target="_blank"><%=entry.getKey()%></a></td>
	<td><%=eachContextDescription%></td>
	</tr>
	<% } %>
	</tbody>
    </table>
    </section>
	</div>
	<% } %>
</body>
</html>