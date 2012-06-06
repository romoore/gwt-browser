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
  public static final String SNAPSHOT_PATH = "/snapshot?uri=";
  public static final String SEARCH_PATH = "/search?uri=";

  private Label errorMsgLabel = new Label();

  private Label searchLabel = new Label("Enter a URI regex to search:");
  private TextBox uriBox = new TextBox();
  private Button searchUriButton = new Button("Search");

  private Label currentSearchLabel = new Label();

  private HorizontalPanel addUriPanel = new HorizontalPanel();
  private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.EM);

  private String currentSearch = "";
  private ArrayList<String> matchingUris = new ArrayList<String>();

  private UriDataProvider dataProvider = new UriDataProvider();
  private WorldModelTreeModel browserModel = new WorldModelTreeModel(this.dataProvider);
  private CellBrowser wmBrowser = new CellBrowser(this.browserModel, null);

  public void onModuleLoad() {
    
//    this.wmBrowser.setSize("20em", "10em");

    this.addUriPanel.add(this.searchLabel);
    this.addUriPanel.add(this.uriBox);
    this.addUriPanel.add(this.searchUriButton);

    mainPanel.addSouth(this.errorMsgLabel,5);
    mainPanel.addNorth(this.addUriPanel,5);
    this.mainPanel.add(this.wmBrowser);
    this.mainPanel.setSize("60em", "40em");

    RootPanel.get("wmBrowserPanel").add(this.mainPanel);

    uriBox.addKeyPressHandler(new KeyPressHandler() {

      @Override
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          searchUri();
        }
      }
    });

    this.searchUriButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        searchUri();
      }
    });
  }

  final void searchUri() {
    String newUri = WMBrowser.this.uriBox.getText().trim();
    this.currentSearch = newUri;
    this.uriBox.setSelectionRange(0, this.uriBox.getText().length());
    this.uriBox.setFocus(true);

    this.dataProvider.setQuery(newUri);

  }

}
