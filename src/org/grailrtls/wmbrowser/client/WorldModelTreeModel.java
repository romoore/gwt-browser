package org.grailrtls.wmbrowser.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class WorldModelTreeModel implements TreeViewModel {

  private final List<WorldState> states;

  /**
   * This selection model is shared across all leaf nodes. A selection model can
   * also be shared across all nodes in the tree, or each set of child nodes can
   * have its own instance. This gives you flexibility to determine how nodes
   * are selected.
   */
  private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

  public void addState(final WorldState state){
    this.states.add(state);
  }
  
  public void clearStates(){
    this.states.clear();
  }
  
  public boolean removeState(final WorldState state){
    for(Iterator<WorldState> iter = this.states.iterator(); iter.hasNext();){
      WorldState someState = iter.next();
      if(someState.getUri().equals(state.getUri())){
        iter.remove();
        return true;
      }
    }
    return false;
  }
  
  public WorldModelTreeModel() {
    // Create a database of information.
    states = new ArrayList<WorldState>();
  }

  /**
   * Get the {@link NodeInfo} that provides the children of the specified value.
   */
  public <T> NodeInfo<?> getNodeInfo(T value) {
    if (value == null) {
      // LEVEL 0.
      // We passed null as the root value. Return the composers.

      // Create a data provider that contains the list of composers.
      ListDataProvider<WorldState> dataProvider = new ListDataProvider<WorldState>(
          this.states);

      // Create a cell to display a composer.
      Cell<WorldState> cell = new AbstractCell<WorldState>() {

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
            WorldState value, SafeHtmlBuilder sb) {
          sb.appendEscaped(value.getUri());

        }
      };

      // Return a node info that pairs the data provider and the cell.
      return new DefaultNodeInfo<WorldState>(dataProvider, cell);
    } else if (value instanceof WorldState) {
      WorldState state = (WorldState)value;
      // LEVEL 1.
      // We want the children of the composer. Return the playlists.
      List<Attribute> attribList = new ArrayList<Attribute>();
      for(int i = 0; i < state.getAttributes().length(); ++i){
        attribList.add(state.getAttributes().get(i));
      }
      ListDataProvider<Attribute> dataProvider = new ListDataProvider<Attribute>(
          attribList);
      Cell<Attribute> cell = new AbstractCell<Attribute>() {
        @Override
        public void render(Context context, Attribute value, SafeHtmlBuilder sb) {
          if (value != null) {
            sb.appendEscaped(value.getName());
          }
        }
      };
      return new DefaultNodeInfo<Attribute>(dataProvider, cell);
    } 
//    else if (value instanceof Attribute) {
//      // LEVEL 2 - LEAF.
//      // We want the children of the playlist. Return the songs.
//      ListDataProvider<String> dataProvider = new ListDataProvider<String>(
//          ((Attribute) value).getOrigin());
//
//      // Use the shared selection model.
//      return new DefaultNodeInfo<String>(dataProvider, new TextCell(),
//          selectionModel, null);
//    }

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
