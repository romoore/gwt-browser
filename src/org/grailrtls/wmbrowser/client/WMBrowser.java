package org.grailrtls.wmbrowser.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellBrowser.Builder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WMBrowser implements EntryPoint {

  public static String QUERY_HOST = "localhost";
  public static String QUERY_PORT = "7011";
  public static String QUERY_PATH = "/grailrest";
  public static final String SNAPSHOT_PATH = "/snapshot?q=";
  public static final String SEARCH_PATH = "/search?q=";

  private Label errorMsgLabel = new Label();

  private Label searchLabel = new Label("Enter an ID regex to search:");
  private TextBox idBox = new TextBox();
  private Button searchIdButton = new Button("Search");

  private Label currentSearchLabel = new Label();

  private HorizontalPanel addIdPanel = new HorizontalPanel();
  private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.EM);

  private String currentSearch = "";
  private ArrayList<String> matchingUris = new ArrayList<String>();

  private IdDataProvider dataProvider = new IdDataProvider();
  private WorldModelTreeModel browserModel = new WorldModelTreeModel(this.dataProvider);
  private Builder<Object> cb = new CellBrowser.Builder<Object>(this.browserModel, null);
 
  private CellBrowser wmBrowser = this.cb.build();

  public void onModuleLoad() {
    
//    this.wmBrowser.setSize("20em", "10em");

    this.addIdPanel.add(this.searchLabel);
    this.addIdPanel.add(this.idBox);
    this.addIdPanel.add(this.searchIdButton);

    this.mainPanel.addSouth(this.errorMsgLabel,5);
    this.mainPanel.addNorth(this.addIdPanel,5);
    this.mainPanel.add(this.wmBrowser);
    this.mainPanel.setSize("60em", "40em");

    RootPanel.get("wmBrowserPanel").add(this.mainPanel);

    this.idBox.addKeyPressHandler(new KeyPressHandler() {

      @Override
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          searchId();
        }
      }
    });

    this.searchIdButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        searchId();
      }
    });
  }

  final void searchId() {
    String newId = WMBrowser.this.idBox.getText().trim();
    this.currentSearch = newId;
    this.idBox.setSelectionRange(0, this.idBox.getText().length());
    this.idBox.setFocus(true);

    this.dataProvider.setQuery(newId);

  }

}
