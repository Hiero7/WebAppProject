<!DOCTYPE html>
<html>
    <script type="text/javascript" src="autosuggest.js"></script>
    <script type="text/javascript" src="suggestions.js"></script>
    <link rel="stylesheet" type="text/css" href="autosuggest.css" />
    <script type="text/javascript" charset="utf-8">         
    window.onload = function () 
    {
        var oTextbox = new AutoSuggestControl(document.getElementById("q"), new ItemSuggestions()); 
    }
    </script>
<style>
.pn
{
visibility:hidden;
height:0px;
width:0px;
margin:0px;
padding:0px;
}
</style>
<head>
    <title>Search Results</title>
</head>
<body>
    <div>
        <FORM NAME="Keyword Search" METHOD="GET" ENCTYPE="multipart/form-data" ACTION="search">
	    Search Query: <input type="text" id= "q" name="q" autocomplete="off">
	    <input type="text" class="pn" name="numResultsToSkip" value=0>
	    <input type="text" class="pn" name="numResultsToReturn" value=10>
	    <input type="submit" value="Submit">
	</FORM>
    </div>

    <div><h2>
    Results for query: "<%= request.getAttribute("query") %>"</h2>
    <p>Showing results <%= (Integer)request.getAttribute("skipped") %> through <%=(Integer)request.getAttribute("skipped")+(Integer)request.getAttribute("returned")%></p>
    <%= request.getAttribute("resultsTable") %>
    </div>
    

    <% if((Integer)request.getAttribute("skipped")>0) { %>
    <FORM NAME="Keyword Search" METHOD="GET" ENCTYPE="multipart/form-data" ACTION="search" style="float:left;">
        <input type="submit" value="Previous">
        <input type="text" class="pn" name="q" value= "<%= request.getAttribute("query") %>">
	<input type="text" class="pn" name="numResultsToSkip" value="<%= (Integer)request.getAttribute("skipped")-10 %>">
	<input type="text" class="pn" name="numResultsToReturn" value="<%= (Integer)request.getAttribute("numToReturn") %>">
    </FORM>
    <% } %>

    <% if((Integer)request.getAttribute("returned")>=10) { %>
    <FORM NAME="Keyword Search" METHOD="GET" ENCTYPE="multipart/form-data" ACTION="search">
        <input type="submit" value="Next">
        <input type="text" class="pn" name="q" value= "<%= request.getAttribute("query") %>">
	<input type="text" class="pn" name="numResultsToSkip" value="<%= (Integer)request.getAttribute("skipped")+10 %>">
	<input type="text" class="pn" name="numResultsToReturn" value="<%= (Integer)request.getAttribute("numToReturn") %>">
    </FORM>
    <% } %>

</body>
</html>
