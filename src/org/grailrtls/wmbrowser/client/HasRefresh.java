package org.grailrtls.wmbrowser.client;

import com.google.gwt.view.client.HasData;

public interface HasRefresh <R extends HasData<WorldState>>{
  public void refresh(HasData<WorldState> display);
}
