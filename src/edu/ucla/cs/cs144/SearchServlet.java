package edu.ucla.cs.cs144;

import edu.ucla.cs.cs144.FieldName;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.SearchResult;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
	String[] queryL = request.getParameterValues("q");
	String query = queryL[0];
	String[] numToSkipL = request.getParameterValues("numResultsToSkip");
	int numToSkip = Integer.parseInt(numToSkipL[0]);
	String[] numToReturnL = request.getParameterValues("numResultsToReturn");
	int numToReturn = Integer.parseInt(numToReturnL[0]);
	
	AuctionSearchClient client = new AuctionSearchClient();
	SearchResult[] results = client.basicSearch(query, numToSkip, numToReturn);
	
	//create table from results
	String resultsString ="<table border=\"1\">\n<tr><td>ItemId</td><td>Name</td></tr>\n";
	for(int i=0;i<results.length;i++)
	{
	   resultsString+="<tr><td><a href=item?id="+results[i].getItemId()+">"+results[i].getItemId()+"</a></td><td>"+results[i].getName()+"</td></tr>\n";
	}
	if(results.length==0)
	{
	   resultsString+="<tr><td></td><td>No items to show</td></tr>\n";
	}
	resultsString+="</table>\n";
	
	request.setAttribute("query", query);
	request.setAttribute("resultsTable", resultsString);
	request.setAttribute("skipped",numToSkip);
	request.setAttribute("returned", results.length);
	request.setAttribute("numToReturn", numToReturn);
        request.getRequestDispatcher("/searchIndex.jsp").forward(request, response);
    }
}
