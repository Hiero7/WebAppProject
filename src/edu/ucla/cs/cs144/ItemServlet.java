package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String queryString = request.getQueryString().substring(3);

        request.setAttribute("itemid", queryString);

        AuctionSearchClient asc = new AuctionSearchClient();

        String returnedXML = asc.getXMLDataForItemId(queryString);

        int lastChar = 0;

        if(returnedXML.equals(""))
        {
            request.setAttribute("found", "false");
            request.setAttribute("location", "");
            request.setAttribute("country", "");

            request.getRequestDispatcher("/itemResult.jsp").forward(request,response);
            return;
        }
        else
        {
            request.setAttribute("found", "true");
        }


        Pattern pat = Pattern.compile("<Name>(.*)</Name>");
        Matcher mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("name", mat.group(1));

        pat = Pattern.compile("Seller UserID=\"([^\"]*)\"");
        mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("sellerid", mat.group(1));

        lastChar = mat.end(1);

        pat = Pattern.compile("Rating=\"([^\"]*)\"");
        mat = pat.matcher(returnedXML);
        mat.find(lastChar);
        request.setAttribute("rating", mat.group(1));

        pat = Pattern.compile("<Currently>(.*)</Currently>");
        mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("currently", mat.group(1));

        pat = Pattern.compile("<Buy_Price>(.*)</Buy_Price>");
        mat = pat.matcher(returnedXML);
        if(mat.find(0))
        {
            request.setAttribute("buyprice", mat.group(1));
        }
        else
        {
            request.setAttribute("buyprice", "");
        }

        pat = Pattern.compile("<First_Bid>(.*)</First_Bid>");
        mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("firstbid", mat.group(1));

        pat = Pattern.compile("<Number_of_Bids>(.*)</Number_of_Bids>");
        mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("numbids", mat.group(1));

        pat = Pattern.compile("<Started>(.*)</Started>");
        mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("started", mat.group(1));

        pat = Pattern.compile("<Ends>(.*)</Ends>");
        mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("ends", mat.group(1));

        pat = Pattern.compile("<Description>(.*)</Description>");
        mat = pat.matcher(returnedXML);
        mat.find(0);
        request.setAttribute("description", mat.group(1));

        pat = Pattern.compile("<Bids>");
        mat = pat.matcher(returnedXML);
        if(mat.find(0))
        {
            ArrayList<BidderInfo> bidList = new ArrayList<BidderInfo>();

            int start = mat.start();
            pat = Pattern.compile("</Bids>");
            mat = pat.matcher(returnedXML);
            mat.find(0);
            int end = mat.end();

            String allBidInfo = returnedXML.substring(start,end);

            pat = Pattern.compile("Bidder UserID=\"([^\"]*)\"");
            mat = pat.matcher(allBidInfo);
            mat.reset();

            int lastFound = 0;

            while(mat.find())
            {
                BidderInfo bidinfo = new BidderInfo();

                bidinfo.id = mat.group(1);

                Pattern newpat = Pattern.compile("Rating=\"([^\"]*)\"");
                Matcher newmat = newpat.matcher(allBidInfo);
                newmat.find(lastFound);
                bidinfo.rating=newmat.group(1);

                newpat = Pattern.compile("<Location>(.*)</Location>");
                newmat = newpat.matcher(allBidInfo);
                if(newmat.find(lastFound))
                {
                    bidinfo.location=newmat.group(1);
                }
                else
                {
                    bidinfo.location="";
                }

                newpat = Pattern.compile("<Country>(.*)</Country>");
                newmat = newpat.matcher(allBidInfo);
                if(newmat.find(lastFound))
                {
                    bidinfo.country=newmat.group(1);
                }
                else
                {
                    bidinfo.country="";
                }

                newpat = Pattern.compile("<Time>(.*)</Time>");
                newmat = newpat.matcher(allBidInfo);
                newmat.find(lastFound);
                bidinfo.time = newmat.group(1);

                newpat = Pattern.compile("<Amount>(.*)</Amount>");
                newmat = newpat.matcher(allBidInfo);
                newmat.find(lastFound);
                bidinfo.amount = newmat.group(1);

                bidList.add(bidinfo);

                lastFound = newmat.end();
            }

            BidderInfo[] bidArray = new BidderInfo[bidList.size()];

            bidList.toArray(bidArray);
            request.setAttribute("bids", bidArray);

            pat = Pattern.compile("</Bids>");
            mat = pat.matcher(returnedXML);
            mat.find(0);
            lastChar = mat.end();

        }
        else
        {
            pat = Pattern.compile("<Bids/>");
            mat = pat.matcher(returnedXML);
            mat.find(0);
            lastChar = mat.end();
        }

        pat = Pattern.compile("<Location>(.*)</Location>");
        mat = pat.matcher(returnedXML);
        mat.find(lastChar);
        request.setAttribute("location", mat.group(1));

        pat = Pattern.compile("<Country>(.*)</Country>");
        mat = pat.matcher(returnedXML);
        mat.find(lastChar);
        request.setAttribute("country", mat.group(1));

        pat = Pattern.compile("<Category>(.*)</Category>");
        mat = pat.matcher(returnedXML);
        mat.reset();

        ArrayList<String> catList = new ArrayList<String>();

        while(mat.find())
        {
            request.setAttribute("debug", "here");
            catList.add(mat.group(1));
        }

        String[] catArray = new String[catList.size()];

        catList.toArray(catArray);

        request.setAttribute("cats", catArray);

        request.getRequestDispatcher("/itemResult.jsp").forward(request,response);
    }
}
