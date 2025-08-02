package ats.display;

import com.ats.tradingsystem.enums.TradeType;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TradeCaptureFormBuilder {

    public VBox createTradeCaptureDropdownForm() {
        VBox container = new VBox(15);
        container.setStyle("-fx-padding: 20;");

        ComboBox<String> tradeTypeSelector = new ComboBox<>();
        for (TradeType t : TradeType.values()) tradeTypeSelector.getItems().add(t.getLabel());
        tradeTypeSelector.setValue(TradeType.OIS_TRADE.getLabel());

        Label dropdownLabel = new Label("Trade Type:");
        HBox selectorRow = new HBox(10, dropdownLabel, tradeTypeSelector);

        StackPane formContainer = new StackPane();
        formContainer.getChildren().add(new TradeFormFactory().createTradeForm(TradeType.OIS_TRADE));

        tradeTypeSelector.setOnAction(e -> {
            formContainer.getChildren().clear();
            TradeType selectedType = TradeType.fromLabel(tradeTypeSelector.getValue());
            formContainer.getChildren().add(new TradeFormFactory().createTradeForm(selectedType));
        });

        container.getChildren().addAll(selectorRow, formContainer);
        return container;
    }
}