package org.grailrtls.wmbrowser.client;

import java.util.ArrayList;
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

public class WSDataProvider extends AsyncDataProvider<WorldState> implements HasRefresh<HasData<WorldState>> {

  private static final Logger log = Logger.getLogger(WSDataProvider.class.getName());
  
  private String query = null;
  private String snapshotPath;

  private ArrayList<WorldState> matchingStates = new ArrayList<WorldState>();

  private int jsonRequestId = 0;

  public WSDataProvider(final String snapshotPath) {
    super();
    this.snapshotPath = snapshotPath;
  }

  public void setQuery(final String query) {
    this.query = query;
    int numStates = this.matchingStates.size();
    this.matchingStates.clear();
    final String searchUrl = URL.encode(this.snapshotPath + this.query)
        + "&callback=";

    this.retrieveSnapshot(this.query);
    this.updateRowCount(0, true);
    
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
      final String url, WSDataProvider handler)/*-{
		var callback = "callback" + requestId;

		var script = document.createElement("script");

		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");

		window[callback] = function(jsonObj) {
			handler.@org.grailrtls.wmbrowser.client.WSDataProvider::handleSearchResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}

		setTimeout(
				function() {
					if (!window[callback + "done"]) {
						handler.@org.grailrtls.wmbrowser.client.WSDataProvider::handleSearchResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
					}

					document.body.removeChild(script);
					delete window[callback];
					delete window[callback + "done"];
				}, 1000);

		document.body.appendChild(script);

  }-*/;

  /**
   * Handle the response to the request for stock data from a remote server.
   */
  public void handleSearchResponse(JavaScriptObject jso) {
    if (jso == null) {

      return;
    }

    

  }

  public void handleWSResponse(JavaScriptObject jso) {
    if (jso == null) {
      return;
    }
    this.updateWorldState(asArrayOfWorldState(jso));
  }

  private final native JsArray<JsWorldState> asArrayOfWorldState(
      JavaScriptObject jso) /*-{
		return jso;
  }-*/;

  private final native JsArrayString asArrayOfString(JavaScriptObject jso) /*-{
		return jso;
  }-*/;

  final void retrieveSnapshot(final String uri) {
    final String url = URL.encode(this.snapshotPath + uri)
        + "&callback=";

    getWSJson(this.jsonRequestId++, url, this);
  }

  protected native static void getWSJson(final int requestId, final String url,
      WSDataProvider handler)/*-{
		var callback = "callback" + requestId;

		var script = document.createElement("script");

		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");

		window[callback] = function(jsonObj) {
			handler.@org.grailrtls.wmbrowser.client.WSDataProvider::handleWSResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}

		setTimeout(
				function() {
					if (!window[callback + "done"]) {
						handler.@org.grailrtls.wmbrowser.client.WSDataProvider::handleWSResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
					}

					document.body.removeChild(script);
					delete window[callback];
					delete window[callback + "done"];
				}, 2000);

		document.body.appendChild(script);

  }-*/;
  
  protected void updateWorldState(JsArray<JsWorldState> states){
    for(int i = 0; i < states.length(); ++i){
      JsWorldState iState = states.get(i);
      WorldState newState = new WorldState();
      newState.setUri(iState.getUri());
      Attribute[] attrs = new Attribute[iState.getAttributes().length()];
      newState.setAttributes(attrs);
      for(int j = 0; j < iState.getAttributes().length(); ++j){
        JsAttribute jAttr = iState.getAttributes().get(j);
        Attribute newAttr = new Attribute();
        newAttr.setCreated(jAttr.getCreated());
        newAttr.setExpires(jAttr.getExpires());
        newAttr.setData(jAttr.getData());
        newAttr.setName(jAttr.getName());
        newAttr.setOrigin(jAttr.getOrigin());
        
        attrs[j] = newAttr;
      }
      this.matchingStates.add(newState);
    }
    
//    for(HasData<WorldState> disp : this.getDataDisplays()){
//      this.onRangeChanged(disp);
//    }
    this.updateRowCount(this.matchingStates.size(), true);
    this.updateRowData(0, this.matchingStates);
  }

  @Override
  public void refresh(HasData<WorldState> display) {
  }
  
}
