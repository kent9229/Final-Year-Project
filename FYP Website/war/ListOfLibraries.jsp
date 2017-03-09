<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.google.appengine.api.datastore.DatastoreService,
    com.google.appengine.api.datastore.DatastoreServiceFactory,
    com.google.appengine.api.datastore.Query,
    com.google.appengine.api.datastore.PreparedQuery,
    com.google.appengine.api.datastore.Entity,
    com.google.appengine.api.datastore.Entities,
    com.google.appengine.api.datastore.FetchOptions,
    com.google.appengine.api.datastore.QueryResultList,
    com.google.appengine.api.datastore.Query.FilterOperator,
    java.util.ArrayList,
    java.util.HashMap,
    java.util.Map"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of supported Java Libraries</title>
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
		$('table.display').dataTable();
	} );
	
	</script>
</head>
<body>
<div class="container">
<section>
<table id = "" class="display" cellspacing="0" width="100%">
	<thead>
	    <tr>
		    <th scope="col">Java Libraries</th>
		   <th scope="col">Description</th>
		    <th scope="col">Similar Libraries</th>
	    </tr>
	</thead>
	<tbody>
    <% 	   	
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    	HashMap<String, String> map = new HashMap<String, String>();
		Query queryMappings = new Query("libraryMappings");
		FetchOptions fetchOptionsMappings = FetchOptions.Builder.withLimit(300);
		QueryResultList<Entity> qrl = datastoreService.prepare(queryMappings).asQueryResultList(fetchOptionsMappings);
		
		int id = 1;
		String libs = "";
		for(Entity u1 : qrl) {
			map.put(u1.getProperty("SourceLibrary").toString(), "");
		}
		
		for (Map.Entry<String, String> entry: map.entrySet()){
			if (libs.equals("")) libs = entry.getKey();
			else libs += ";" + entry.getKey();
		}
		
		for (Map.Entry<String, String> entry: map.entrySet()){
			String lib = entry.getKey();
	%>
		<tr>
			<td><%=lib%></td>
			<td id="<%=lib%>"></td>
			<td align="center">
			<%
			Query query = new Query("libraryMappings").addFilter("SourceLibrary", FilterOperator.EQUAL, lib);
			FetchOptions fetchOptions = FetchOptions.Builder.withLimit(300);
			QueryResultList<Entity> qrl1 = datastoreService.prepare(query).asQueryResultList(fetchOptions);
			for(Entity u2 : qrl1) {
				String targetLibrary = u2.getProperty("TargetLibrary").toString();
			%>
			
			<a href = "FilteredData.jsp?id=<%=lib%> vs <%=targetLibrary%>"><%=targetLibrary%></a>
			<% } %>
			</td>
		</tr>
	<% } %>
	</tbody>
    </table>
    </section>
    </div>
    <script>
    function getLibDesc(library){
		var libArray = library.split(";");
		var strToQuery = "";
		
		for (i = 0; i < libArray.length; i++){
			if (strToQuery == "") strToQuery = libArray[i];
			else strToQuery += ";" + libArray[i];
			
			if ((i + 1) % 20 == 0 || i == libArray.length - 1){
				var strToQueryArray = strToQuery.split(";");
				var tagWiki = JSON.parse($.ajax({
				type:"GET",url:"https://api.stackexchange.com/2.2/tags/" + strToQuery + "/wikis?site=stackoverflow",async:false}).responseText);
				
				for (j = 0; j < tagWiki["items"].length; j++){
					var lib = tagWiki["items"][j]["tag_name"];
					var desc = tagWiki["items"][j]["excerpt"];
					document.getElementById(lib).innerHTML = desc;
				}
				strToQuery = "";
			}
		}
	}
    </script>
    <script>getLibDesc("<%=libs%>");</script>
</body>
</html>