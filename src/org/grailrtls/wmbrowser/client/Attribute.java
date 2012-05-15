package org.grailrtls.wmbrowser.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public class Attribute {

  private String name;
  private String origin;
  private String data;
  private String created;
  private String expires;
  
  protected Attribute(){}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getExpires() {
    return expires;
  }

  public void setExpires(String expires) {
    this.expires = expires;
  }
 
  
}
