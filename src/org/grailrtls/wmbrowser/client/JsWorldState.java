package org.grailrtls.wmbrowser.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


public class JsWorldState extends JavaScriptObject{
  protected JsWorldState(){
    
  }
  
  public final native void setIdentifier(final String identifier) /*-{ this.identifier = identifier;}-*/;
  
  public final native String getIdentifier() /*-{ return this.identifier; }-*/;
  
  public final native JsArray<JsAttribute> getAttributes() /*-{ return this.attributes; }-*/;
  
  public final native void setAttributes(final JsArray<JsAttribute> attributes) /*-{ this.attributes = attributes; }-*/;

}
