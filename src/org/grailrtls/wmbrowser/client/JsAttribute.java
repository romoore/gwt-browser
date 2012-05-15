package org.grailrtls.wmbrowser.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public class JsAttribute extends JavaScriptObject {

  protected JsAttribute(){}
  
  public final native String getName() /*-{ return this.attributeName; }-*/;
  
  public final native String getOrigin() /*-{ return this.origin; }-*/;
  
  public final native String getData() /*-{ return this.data; }-*/;
  
  public final native String getCreated() /*-{ return this.creationDate; }-*/;
  
  public final native String getExpires() /*-{ return this.expirationDate; }-*/;
  
}
