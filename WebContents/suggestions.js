// some globals for handling responses
var g_oAutoSuggestControl;
var g_bTypeAhead;
var g_iQueryIndex = 0;

function ItemSuggestions() {
    this.g_xmlhttp=new XMLHttpRequest();
    this.items = [];
}

ItemSuggestions.prototype.sendInfo=function(oAutoSuggestControl)
{
//    console.log("sending info");	
    this.g_xmlhttp = new XMLHttpRequest();

    var itemSugObj = this;
/*
    //g_iQueryIndex will let us keep a unique identifier with the response
    // handler for each XMLHttpRequest.  That way, when a response comes in, if
    // it's not a response to the most recent request, we'll discard it.
    g_iQueryIndex++;
    var tempId = g_iQueryIndex; */
    
    this.g_xmlhttp.onreadystatechange=
        function () {handleResponse(itemSugObj/*, tempId*/)};

    console.log(oAutoSuggestControl.textbox.value);
//    console.log("info sending: query " + tempId);
    this.g_xmlhttp.open("GET", "suggest?q=" + 
        escape(oAutoSuggestControl.textbox.value)); 

    this.g_xmlhttp.send(null);
} 

function handleResponse(itemSugObj/*, handlerId*/) 
{
/*    if (handlerId != g_iQueryIndex)
    {
        console.log("late response from query " + handlerId + ", state " + 
                itemSugObj.g_xmlhttp.readyState);
        return;
    }
    console.log("timely response from query " + handlerId + ", state " +
                itemSugObj.g_xmlhttp.readyState);*/
    if (itemSugObj.g_xmlhttp.readyState == 4)
    {
        var returnedInfo = [];
        var suggestionNodes = itemSugObj.g_xmlhttp.responseXML.documentElement.children;
        var suggestedText;
        var numQueries;
//        console.log("getting suggestions");
        for (var ii = 0; ii < suggestionNodes.length; ii++)
        {
          suggestedText = suggestionNodes[ii].children[0].attributes[0].value;

	  returnedInfo.push(suggestedText);
        }
//	console.log("new items found");
	itemSugObj.items=returnedInfo;
    g_oAutoSuggestControl.autosuggest(returnedInfo, g_bTypeAhead);
    }
}
/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
ItemSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {

//        console.log("Value at suggestions:71: " + oAutoSuggestControl.textbox.value);
    g_oAutoSuggestControl = oAutoSuggestControl;
    g_bTypeAhead = bTypeAhead;

    var aSuggestions = [];
    this.sendInfo(oAutoSuggestControl);
//    aSuggestions = this.items;
    //provide suggestions to the control
//    oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
};
