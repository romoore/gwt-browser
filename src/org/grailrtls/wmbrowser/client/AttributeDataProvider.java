package org.grailrtls.wmbrowser.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.javascript.host.Attr;
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
 * Provides a single WorldState (primarily its attributes) by requesting a
 * snapshot from the world model server.
 * 
 * @author Robert Moore
 * 
 */
public class AttributeDataProvider extends AsyncDataProvider<Attribute> {

  private static final Logger log = Logger
      .getLogger(AttributeDataProvider.class.getName());

  private final String searchPath = "http://" + WMBrowser.QUERY_HOST+":" + WMBrowser.QUERY_PORT + WMBrowser.QUERY_PATH + WMBrowser.SNAPSHOT_PATH;

  private String identifier = null;

  private List<Attribute> theAttributes = new ArrayList<Attribute>();

  private int jsonRequestId = 0;
  
  private AttributeNameComparator comparator = new AttributeNameComparator();

  public AttributeDataProvider(final String identifier) {
    super();

    this.identifier = identifier;
    this.retrieveSnapshot(identifier);
  }

  @Override
  protected void onRangeChanged(HasData<Attribute> display) {
    final Range range = display.getVisibleRange();
    int rStart = range.getStart();
    int rLength = range.getLength();
    int rEnd = rStart + rLength;
    List<Attribute> visibleAttributes = new LinkedList<Attribute>();
    if (rStart < this.theAttributes.size()) {
      for (int i = rStart; i < rEnd && i < this.theAttributes.size(); ++i) {
        visibleAttributes.add(this.theAttributes.get(i));
      }
    }
    this.updateRowData(rStart, visibleAttributes);
  }

  protected native static void getSearchJson(final int requestId,
      final String url, AttributeDataProvider handler)/*-{
		var callback = "callback" + requestId;

		var script = document.createElement("script");

		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");

		window[callback] = function(jsonObj) {
			handler.@org.grailrtls.wmbrowser.client.AttributeDataProvider::handleSearchResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}

		setTimeout(
				function() {
					if (!window[callback + "done"]) {
						handler.@org.grailrtls.wmbrowser.client.AttributeDataProvider::handleSearchResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
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

  final void retrieveSnapshot(final String identifier) {
    final String url = URL.encode(this.searchPath + identifier) + "&cb=";

    getWSJson(this.jsonRequestId++, url, this);
  }

  protected native static void getWSJson(final int requestId, final String url,
      AttributeDataProvider handler)/*-{
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

  protected void updateWorldState(JsArray<JsWorldState> states) {
    this.theAttributes.clear();
    if (states == null || states.length() == 0) {
      return;
    }
    JsWorldState iState = states.get(0);
    for (int j = 0; j < iState.getAttributes().length(); ++j) {
      JsAttribute jAttr = iState.getAttributes().get(j);
      Attribute newAttr = new Attribute();
      newAttr.setCreated(jAttr.getCreated());
      newAttr.setExpires(jAttr.getExpires());
      newAttr.setData(jAttr.getData());
      newAttr.setName(jAttr.getName());
      newAttr.setOrigin(jAttr.getOrigin());
      this.theAttributes.add(newAttr);
    }
    
    Collections.sort(this.theAttributes, this.comparator);

    this.updateRowCount(this.theAttributes.size(), true);
    this.updateRowData(0, this.theAttributes);
  }

}
