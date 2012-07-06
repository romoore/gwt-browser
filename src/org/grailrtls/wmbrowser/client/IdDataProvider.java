package org.grailrtls.wmbrowser.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.http.client.URL;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.xml.internal.ws.api.server.WSWebServiceContext;

/**
 * Queries for a list of Identifier values from the World Model.
 * @author Robert Moore.
 *
 */
public class IdDataProvider extends AsyncDataProvider<WorldState>  {

  private static final Logger log = Logger.getLogger(IdDataProvider.class.getName());
  
  private String query = null;
  private final String searchPath ="http://" + WMBrowser.QUERY_HOST+":" + WMBrowser.QUERY_PORT + WMBrowser.QUERY_PATH + WMBrowser.SEARCH_PATH;

  private ArrayList<WorldState> matchingStates = new ArrayList<WorldState>();

  private int jsonRequestId = 0;
  
  private WorldStateComparator comparator = new WorldStateComparator();

  public IdDataProvider() {
    super();
  }

  public void setQuery(final String query) {
    this.query = query;
    
    this.matchingStates.clear();

    this.retrieveSearchResults(this.query);
   
    
  }

  @Override
  protected void onRangeChanged(HasData<WorldState> display) {
    final Range range = display.getVisibleRange();
    int rStart = range.getStart();
    int rLength = range.getLength();
    int rEnd = rStart + rLength;
    List<WorldState> visibleStates = new LinkedList<WorldState>();
    if(rStart < this.matchingStates.size()){
      for(int i = rStart; i < rEnd && i < this.matchingStates.size(); ++i){
        visibleStates.add(this.matchingStates.get(i));
      }
    }
    this.updateRowData(rStart, visibleStates);
  }

  protected native static void getSearchJson(final int requestId,
      final String url, IdDataProvider handler)/*-{
		var callback = "callback" + requestId;

		var script = document.createElement("script");

		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");

		window[callback] = function(jsonObj) {
			handler.@org.grailrtls.wmbrowser.client.IdDataProvider::handleSearchResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}

		setTimeout(
				function() {
					if (!window[callback + "done"]) {
						handler.@org.grailrtls.wmbrowser.client.IdDataProvider::handleSearchResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
					}

					document.body.removeChild(script);
					delete window[callback];
					delete window[callback + "done"];
				}, 2000);

		document.body.appendChild(script);

  }-*/;

  /**
   * Handle the response to the request for stock data from a remote server.
   */
  public void handleSearchResponse(JavaScriptObject jso) {
    if (jso == null) {

      return;
    }

    this.updateWorldState(asArrayOfString(jso));

  }

  public void handleWSResponse(JavaScriptObject jso) {
    if (jso == null) {
      return;
    }
    
  }

  private final native JsArray<JsWorldState> asArrayOfWorldState(
      JavaScriptObject jso) /*-{
		return jso;
  }-*/;

  private final native JsArrayString asArrayOfString(JavaScriptObject jso) /*-{
		return jso;
  }-*/;

  final void retrieveSearchResults(final String identifier) {
    final String url = URL.encode(this.searchPath + identifier)
        + "&cb=";

    getSearchJson(this.jsonRequestId++, url, this);
  }

  protected native static void getWSJson(final int requestId, final String identifier,
      IdDataProvider handler)/*-{
		var callback = "callback" + requestId;

		var script = document.createElement("script");

		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");

		window[callback] = function(jsonObj) {
			handler.@org.grailrtls.wmbrowser.client.AttributeDataProvider::handleWSResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}

		setTimeout(
				function() {
					if (!window[callback + "done"]) {
						handler.@org.grailrtls.wmbrowser.client.AttributeDataProvider::handleWSResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
					}

					document.body.removeChild(script);
					delete window[callback];
					delete window[callback + "done"];
				}, 2000);

		document.body.appendChild(script);

  }-*/;
  
  protected void updateWorldState(JsArrayString ids){
    this.matchingStates.clear();
    for(int i = 0; i < ids.length(); ++i){
      String someIdentifier = ids.get(i);
      WorldState newState = new WorldState();
      newState.setUri(someIdentifier);
      
      this.matchingStates.add(newState);
    }
    
    Collections.sort(this.matchingStates,this.comparator);
    
    this.updateRowCount(this.matchingStates.size(), true);
    this.updateRowData(0, this.matchingStates);
  }

}
