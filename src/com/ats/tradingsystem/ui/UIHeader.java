package com.ats.tradingsystem.ui;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class UIHeader {

    public HBox createTitleBar() {
        HBox titleBar = new HBox(20);
        titleBar.setStyle("-fx-padding: 10; -fx-alignment: center-left; -fx-background-color: #2C3E50;");

        Image logo = new Image(getClass().getResource("/ATSLogo.png").toExternalForm());
        ImageView imageView = new ImageView(logo);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        Label title = new Label("Trading System");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label user = new Label("Logged in as: trader_user");
        user.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button closeButton = new Button("Exit");
        closeButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) Platform.exit();
            });
        });

        titleBar.getChildren().addAll(imageView, title, spacer, user, closeButton);
        return titleBar;
    }
}
