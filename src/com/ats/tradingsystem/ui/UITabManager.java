package com.ats.tradingsystem.ui;

import ats.display.TradeCaptureFormBuilder;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class UITabManager {
    private final TabPane bottomTabPane;
    private final VBox bottomWrapper;

    public UITabManager() {
        bottomTabPane = new TabPane();
        bottomTabPane.setPrefHeight(140);
        bottomWrapper = new VBox(bottomTabPane);
    }

    public VBox getBottomWrapper() {
        return bottomWrapper;
    }

    public TabPane createMainTabPane() {
        TabPane tabPane = new TabPane();

        Tab tradeCaptureTab = new Tab("Trade Capture");
        tradeCaptureTab.setContent(new TradeCaptureFormBuilder().createTradeCaptureDropdownForm());
        tradeCaptureTab.setClosable(false);

        Tab marketDataTab = new Tab("Market Data");
        marketDataTab.setContent(new Label("Market Data Module"));
        marketDataTab.setClosable(false);

        Tab reportsTab = new Tab("Reports");
        reportsTab.setContent(new Label("Reports Module"));
        reportsTab.setClosable(false);

        tabPane.getTabs().addAll(tradeCaptureTab, marketDataTab, reportsTab);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            updateBottomTabs(newTab.getText());
        });

        updateBottomTabs("Trade Capture");
        return tabPane;
    }

    private void updateBottomTabs(String moduleName) {
        bottomTabPane.getTabs().clear();
        switch (moduleName) {
            case "Trade Capture" -> {
                bottomTabPane.getTabs().add(createTab("Charts", "Price and volume charts"));
                bottomTabPane.getTabs().add(createTab("Profit & Loss", "P&L calculations"));
            }
            case "Market Data" -> {
                bottomTabPane.getTabs().add(createTab("Live Feeds", "Streaming market data"));
                bottomTabPane.getTabs().add(createTab("Trends", "Market trend analysis"));
            }
            case "Reports" -> {
                bottomTabPane.getTabs().add(createTab("Summary", "Report summaries"));
                bottomTabPane.getTabs().add(createTab("Export", "Export to PDF/CSV"));
            }
        }
    }

    private Tab createTab(String title, String contentText) {
        Tab tab = new Tab(title);
        tab.setContent(new Label(contentText));
        tab.setClosable(false);
        return tab;
    }
}
