package org.grailrtls.wmbrowser.client;

import java.util.Comparator;

public class AttributeNameComparator implements Comparator<Attribute> {

  @Override
  public int compare(Attribute arg0, Attribute arg1) {
    int compared = arg0.getName().compareTo(arg1.getName());
    if (compared != 0) {
      return compared;
    }

    compared = arg0.getOrigin().compareTo(arg1.getOrigin());

    return compared;

  }

}
