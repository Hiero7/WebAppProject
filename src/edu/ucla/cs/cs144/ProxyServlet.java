package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
       response.setContentType("text/xml");
        String searchText = request.getQueryString().substring(2);
        PrintWriter out = response.getWriter();

        // Much of this code was taken from the URLConnection Tutorial on
        //  the Oracle Java Implementation.
        
        // Create the connection object to the Google auto-complete service
        HttpURLConnection conn = (HttpURLConnection) 
            (new URL("http://google.com/complete/search?output=toolbar&q=" 
                        + searchText)).openConnection();


        // Direct the returned page at the URL to a BufferedReader
        BufferedReader page = new BufferedReader(
                                new InputStreamReader(
                                  conn.getInputStream()));

        String eachLine;

        // While there's stuff to read from the page...
        while((eachLine = page.readLine()) != null)
        {
            // Send it back to the html page that called this servlet
            out.println(eachLine);
        }

        page.close();

        out.close();
    }
}
