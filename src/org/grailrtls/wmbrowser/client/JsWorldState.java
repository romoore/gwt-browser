package org.grailrtls.wmbrowser.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


public class JsWorldState extends JavaScriptObject{
  protected JsWorldState(){
    
  }
  
  public final native void setUri(final String uri) /*-{ this.uri = uri;}-*/;
  
  public final native String getUri() /*-{ return this.uri; }-*/;
  
  public final native JsArray<JsAttribute> getAttributes() /*-{ return this.attributes; }-*/;
  
  public final native void setAttributes(final JsArray<JsAttribute> attributes) /*-{ this.attributes = attributes; }-*/;

}
