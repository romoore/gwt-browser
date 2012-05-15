package org.grailrtls.wmbrowser.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class SearchResponse extends JavaScriptObject{
  protected SearchResponse(){
    
  }
  
  public final native String[] getUris() /*-{ return this.uris; }-*/;

}
