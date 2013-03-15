<!DOCTYPE html>

<%@ page import="edu.ucla.cs.cs144.BidderInfo" %>

<html>
  <head>
    <title>
      Item Results
    </title>
    <link rel="stylesheet" href="item.css">
  </head>
  <body onLoad="startup();">

    <div id="allcontent">

    <div id="formwrap">
      <div class="inline">
        <form method="GET" action="item">
          <input type="text" name="id" autocomplete="off"/>
          <input type="submit" value="Find Item"/>
        </form>
      </div>
      <div class="inline">
        <input id="hideButton" type="button" value="-" onClick="hideInfo();"/>
      </div>
    </div> <!-- end formwrap -->

    <div id="infoWrap">

    <br>
    <hr>

    <%if (request.getAttribute("found").equals("false")) 
      { %>
    <h1>No item found with item id "<%= request.getAttribute("itemid") %>"</h1>
    <%} 
      else 
      { %>

        <h1>Item #<%= request.getAttribute("itemid") %>:</h1><br>
        <b>Item Name: </b><%= request.getAttribute("name") %><br>
        <b>On sale by: </b><%= request.getAttribute("sellerid") %> (Rating:
        <%= request.getAttribute("rating") %> |  
        <%= request.getAttribute("location") %>, 
        <%= request.getAttribute("country") %>)<br>
        <b>Auction runs: </b><%= request.getAttribute("started") %> - <%= request.getAttribute("ends") %><br>

        <b>Current price: </b><%= request.getAttribute("currently") %><br>
        <%if (!request.getAttribute("buyprice").equals("")) 
          { %>
            <b>Buy Now price: </b><%= request.getAttribute("buyprice") %><br>
        <%} %>

        <b>Starting price: </b><%= request.getAttribute("firstbid") %><br>
        <b>Number of bids placed: </b><%= request.getAttribute("numbids") %> <br>

        <%if (request.getAttribute("numbids").equals("0")) 
          { %>
            <p>No bids have been placed on this item.</p>
        <%} 
          else 
          { %>
            <p><h2>Bids</h2></p> 
            <%for (BidderInfo eachBid : (BidderInfo[]) request.getAttribute("bids")) 
              { %>
              <p><%= eachBid.amount %> - <%= eachBid.time %><br>
                -   <%= eachBid.id %>  
                (Rating:
                <%= eachBid.rating %>
                | <%= eachBid.location %>, <%= eachBid.country %>
                )</p>
            <%}
          }%>

        <p><h2>Categories:</h2></p>
        <%for (String cat : (String[]) request.getAttribute("cats")) 
          { %>
            <%= cat%><br>
        <%} %>

        <p><h2>Description:</h2></p>
        <%= request.getAttribute("description") %>)<br>
    <%}%>
     
      </div>

      </div>
      <div id="mapwrap">
        <div id="mapdiv" style="width:100%; height:100%;">
      </div>
    </div>


        

    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAdYRhasqWhyk2RFY1FAlL0uQtCdX2HoYA&sensor=false">
    </script>

    <script type="text/javascript" charset="utf-8">
      var g_geocoder;
      var g_map;
      var g_itemfound;
      var g_infoShown = true;

      function startup()
      {
        g_geocoder = new google.maps.Geocoder();

        var centerCoords = new google.maps.LatLng(0,0);

        var mapOptions = {
          zoom: 2,
          center: centerCoords,
          mapTypeId: google.maps.MapTypeId.ROADMAP,

          panControl: true,
          panControlOptions: {
            position: google.maps.ControlPosition.RIGHT_TOP
          },

          zoomControl: true,
          zoomControlOptions: {
            position: google.maps.ControlPosition.RIGHT_TOP
          },

          scrollwheel: false,

          streetViewControl: false

        }

        g_map = new google.maps.Map(document.getElementById("mapdiv"), mapOptions);

        g_geocoder.geocode( { 'address': "<%= request.getAttribute("location")%>" + ", " +
                                         "<%= request.getAttribute("country")%>" }, geocoderCallback);

      }


      function geocoderCallback(results, stat)
      {
        if (stat == google.maps.GeocoderStatus.OK)
        {
          g_map.fitBounds(results[0].geometry.viewport);
          g_map.panBy(-200, 0);

        }
        else
        {
          // Leave the map in the full world state
        }
      }

      function hideInfo(ev)
      {
        if (g_infoShown)
        {
          document.getElementById("infoWrap").style.display="none";
          document.getElementById("hideButton").value="+";
          g_infoShown = false;
        }
        else
        {
          document.getElementById("infoWrap").style.display="block";
          document.getElementById("hideButton").value="-";
          g_infoShown = true;
        }
      }


          
    </script>

  </body>

</html>
