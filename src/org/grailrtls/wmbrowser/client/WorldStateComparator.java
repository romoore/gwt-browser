package org.grailrtls.wmbrowser.client;

import java.util.Comparator;

public class WorldStateComparator implements Comparator<WorldState> {

  @Override
  public int compare(WorldState o1, WorldState o2) {
    return o1.getUri().compareTo(o2.getUri());
  }

}
