package com.ats.tradingsystem;

import com.ats.tradingsystem.ui.UIHeader;
import com.ats.tradingsystem.ui.UITabManager;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Get screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // UI components
        UIHeader header = new UIHeader();
        UITabManager tabManager = new UITabManager();

        // Create scrollable content (center + bottom)
        VBox scrollableContent = new VBox();
        scrollableContent.getChildren().addAll(
                tabManager.createMainTabPane(),
                tabManager.getBottomWrapper()
        );

        ScrollPane scrollPane = new ScrollPane(scrollableContent);
        scrollPane.setFitToWidth(true); // makes the scroll area match the window's width

        // Set up layout
        BorderPane root = new BorderPane();
        root.setTop(header.createTitleBar());
        root.setCenter(scrollPane);

        // Scene setup
        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Trading System");
        primaryStage.setScene(scene);
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
