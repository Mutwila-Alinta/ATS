package ats.display;

/*
 * Bottom Tab Manager: BottomTabManager.java
 */
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class BottomTabManager {
    private VBox wrapper;
    private TabPane bottomTabs;

    public BottomTabManager() {
        wrapper = new VBox();
        bottomTabs = new TabPane();
        bottomTabs.setPrefHeight(140);
        wrapper.getChildren().add(bottomTabs);
    }

    public VBox getView() {
        return wrapper;
    }

    public void updateTabs(String moduleName) {
        bottomTabs.getTabs().clear();
        switch (moduleName) {
            case "Trade Capture":
                bottomTabs.getTabs().addAll(createTab("Charts", "Price and volume charts"),
                        createTab("Profit & Loss", "P&L calculations"));
                break;
            case "Market Data":
                bottomTabs.getTabs().addAll(createTab("Live Feeds", "Streaming market data"),
                        createTab("Trends", "Market trend analysis"));
                break;
            case "Reports":
                bottomTabs.getTabs().addAll(createTab("Summary", "Report summaries"),
                        createTab("Export", "Export to PDF/CSV"));
                break;
        }
    }

    private Tab createTab(String title, String content) {
        Tab tab = new Tab(title);
        tab.setContent(new Label(content));
        tab.setClosable(false);
        return tab;
    }
}
