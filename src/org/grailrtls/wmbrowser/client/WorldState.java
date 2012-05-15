package org.grailrtls.wmbrowser.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


public class WorldState{
  private String uri;
  private Attribute[] attributes;
  
  protected WorldState(){
    
  }
  
  public final void setUri(final String uri)  {this.uri = uri;}
  
  public final String getUri() {return this.uri;}
  
  public final Attribute[] getAttributes() { return this.attributes; }
  
  public final void setAttributes(final Attribute[] attributes) { this.attributes = attributes; }

}
