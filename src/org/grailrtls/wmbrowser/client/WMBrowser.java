package org.grailrtls.wmbrowser.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WMBrowser implements EntryPoint {

  protected String requestUri = "";

  protected static final String JSON_URL = "http://localhost:9998/grailrest/snapshot?uri=";

  // private HashMap<String, Attribute[]> currentAttrByUri = new HashMap<String,
  // Attribute[]>();

  // private HashSet<String> requestedUris = new HashSet<String>();

  private Label errorMsgLabel = new Label();
  private HorizontalPanel addUriPanel = new HorizontalPanel();
  private VerticalPanel mainPanel = new VerticalPanel();
//  private FlexTable stocksFlexTable = new FlexTable();
  private TextBox uriBox = new TextBox();
  private Label lastUpdateLabel = new Label("");
  private int jsonRequestId = 0;
  
  private WorldModelTreeModel browserModel = new WorldModelTreeModel();
  private CellBrowser wmBrowser = new CellBrowser(this.browserModel,null);

  public void onModuleLoad() {


    this.addUriPanel.add(this.uriBox);

    mainPanel.add(this.errorMsgLabel);
//    mainPanel.add(this.stocksFlexTable);
    this.mainPanel.add(this.wmBrowser);
    mainPanel.add(this.addUriPanel);
    mainPanel.add(this.lastUpdateLabel);

    RootPanel.get("wmBrowserPanel").add(this.mainPanel);

    Timer timer = new Timer() {

      @Override
      public void run() {
        WMBrowser.this.refreshWMData();

      }
    };
    timer.scheduleRepeating(10000);

    uriBox.addKeyPressHandler(new KeyPressHandler() {

      @Override
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          String newUri = WMBrowser.this.uriBox.getText().trim();
          WMBrowser.this.requestUri = newUri;
          WMBrowser.this.refreshWMData();
        }
      }
    });
  }

  
  final void searchUri(final String uri){
    
  }
  
  
  final void refreshWMData() {

   

    final String url = URL.encode(JSON_URL + this.requestUri) + "&callback=";
    
    getJson(this.jsonRequestId++, url, this);
  }

  protected native static void getJson(final int requestId, final String url,
      WMBrowser handler)/*-{
		var callback = "callback" + requestId;

		var script = document.createElement("script");
		
		
		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");
		
		window[callback] = function(jsonObj) {
		   handler.@org.grailrtls.wmbrowser.client.WMBrowser::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
		   window[callback + "done"] = true;
		}
		
		setTimeout(function(){
		  if(!window[callback+"done"]){
		    handler.@org.grailrtls.wmbrowser.client.WMBrowser::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
		  }
		  
		  document.body.removeChild(script);
		  delete window[callback];
		  delete window[callback+"done"];
		}, 1000);
		
		document.body.appendChild(script);
		
  }-*/;

  protected void updateWmTable(JsArray<WorldState> states) {
    for (int i = 0; i < states.length(); ++i) {
      String uri = states.get(i).getUri();
      JsArray<Attribute> attribs = states.get(i).getAttributes();
      for (int j = 0; j < attribs.length(); ++j) {
        this.updateWmTable(uri, attribs.get(j));
      }
    }
    this.lastUpdateLabel.setText("Last update: " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM).format(new Date()));
    this.errorMsgLabel.setVisible(false);
  }

  protected void updateWmTable(String uri, Attribute attribute) {

    
  }

  private final native JsArray<WorldState> asArrayOfWorldState(JavaScriptObject jso) /*-{
		return jso;
  }-*/;

  private void displayError(final String message) {
    this.errorMsgLabel.setText("Error: " + message);
    this.errorMsgLabel.setVisible(true);
  }
  
  /**
   * Handle the response to the request for stock data from a remote server.
   */
  public void handleJsonResponse(JavaScriptObject jso) {
    if (jso == null) {
      displayError("Couldn't retrieve JSON");
      return;
    }

    updateWmTable(asArrayOfWorldState(jso));

  }
}
