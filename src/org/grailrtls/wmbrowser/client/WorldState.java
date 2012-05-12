package org.grailrtls.wmbrowser.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class WorldState extends JavaScriptObject{
  protected WorldState(){
    
  }
  
  public final native String getUri() /*-{ return this.uri; }-*/;
  
  public final native JsArray<Attribute> getAttributes() /*-{ return this.attributes; }-*/;

}
