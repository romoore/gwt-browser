package org.grailrtls.wmbrowser.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class WorldModelTreeModel implements TreeViewModel {

  /**
   * This selection model is shared across all leaf nodes. A selection model can
   * also be shared across all nodes in the tree, or each set of child nodes can
   * have its own instance. This gives you flexibility to determine how nodes
   * are selected.
   */
  private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
  // private final WSDataProvider dataProvider = new WSDataProvider();

  private final IdDataProvider dataProvider;

  public WorldModelTreeModel(final IdDataProvider dataProvider) {
    this.dataProvider = dataProvider;

  }

  /**
   * Get the {@link NodeInfo} that provides the children of the specified value.
   */
  public <T> NodeInfo<?> getNodeInfo(T value) {
    if (value == null) {
      // LEVEL 0.
      // We passed null as the root value. Return the URIs.

      // Create a data provider that contains the list of WorldState values.

      // Create a cell to display a World State.
      Cell<WorldState> cell = new AbstractCell<WorldState>() {

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
            WorldState value, SafeHtmlBuilder sb) {
          sb.appendEscaped(value.getUri());

        }
      };
      // Return a node info that pairs the data provider and the cell.
      return new DefaultNodeInfo<WorldState>(this.dataProvider, cell);
    } else if (value instanceof WorldState) {
      WorldState state = (WorldState) value;
      // LEVEL 1.
      // We want the children of the World State. Return the Attributes.
    
      Cell<Attribute> cell = new AbstractCell<Attribute>() {
        @Override
        public void render(Context context, Attribute value, SafeHtmlBuilder sb) {
          if (value != null) {
            sb.appendEscaped(value.getName());
          }
        }
      };
      AttributeDataProvider dataProvider = new AttributeDataProvider(state.getUri());
      return new DefaultNodeInfo<Attribute>(dataProvider, cell);
    } else if (value instanceof Attribute) {
      Attribute attrib = (Attribute) value;
      // LEVEL 2 - LEAF.
      // We want the children of the Attributet. Return each component.
      List<String> attributeList = new ArrayList<String>();

      attributeList.add("Value: " + attrib.getData());
      attributeList.add("Origin: " + attrib.getOrigin());
      attributeList.add("Created: " + attrib.getCreated());
      attributeList.add("Expires: " + attrib.getExpires());
      ListDataProvider<String> attrDataProvider = new ListDataProvider<String>(
          attributeList);
      // Use the shared selection model.
      return new DefaultNodeInfo<String>(attrDataProvider, new TextCell(),
          this.selectionModel, null);
    }

    return null;
  }

  /**
   * Check if the specified value represents a leaf node. Leaf nodes cannot be
   * opened.
   */
  public boolean isLeaf(Object value) {
    // The leaf nodes are the songs, which are Strings.
    if (value instanceof String) {
      return true;
    }
    return false;
  }

}
