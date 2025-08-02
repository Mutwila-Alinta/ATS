package ats.display;

/*
 * TradeFormFactory.java
 */
import com.ats.pricing.enums.Currencies;
import com.ats.pricing.enums.DayCountConventionType;
import com.ats.pricing.foundation.TransactionInfo;
import com.ats.pricing.instruments.InstrumentType;
import com.ats.tradingsystem.enums.BuySell;
import com.ats.tradingsystem.enums.SampleCounterparties;
import com.ats.tradingsystem.enums.SamplePortfolios;
import com.ats.tradingsystem.enums.TradeType;
import com.ats.tradingsystem.utils.TradingBook;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TradeFormFactory {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
    LocalDate today = LocalDate.now();
    TransactionInfo transactionInfo = null;

    public GridPane createTradeForm(TradeType tradeType) {
        GridPane form = new GridPane();
        form.setHgap(30);
        form.setVgap(10);
        form.setStyle("-fx-padding: 20;");

        TextField tradeDateField = new TextField(dateFormatter.format(LocalDate.now()));

        DatePicker dpCaptureDate = new DatePicker(today);
        TextField tfTradeID = new TextField();
        TextField tfTradeStatus = new TextField();
        TextField tfTradeSourceSystem = new TextField();
        TextField tfUsername = new TextField();
        TextField tfCounterparty = new TextField();
        TextField tfPortfolio = new TextField();
        TextField tfInstrumentType = new TextField();
        TextField tfInstrumentNotes = new TextField();
        TextField tfUnits = new TextField("1");
        TextField tfBaseCurrency = new TextField();
        TextField tfBuySell = new TextField();
        TextField tfFundingStatus = new TextField("Unfunded");
        TextField tfCSAType = new TextField("No Csa Agreement");
        TextField tfRiskDefinition = new TextField("TRADING");

        ComboBox<SamplePortfolios> portfoliosCombo = new ComboBox<>();
        portfoliosCombo.getItems().addAll(SamplePortfolios.values());
        //Set default selected value
        portfoliosCombo.setValue(SamplePortfolios.Book1);

        ComboBox<SampleCounterparties> counterpartiesCombo = new ComboBox<>();
        counterpartiesCombo.getItems().addAll(SampleCounterparties.values());
        //Set default selected value
        counterpartiesCombo.setValue(SampleCounterparties.MtN);

        ComboBox<BuySell> buySellCombo = new ComboBox<>();
        buySellCombo.getItems().addAll(BuySell.values());
        //Set default selected value
        buySellCombo.setValue(BuySell.Buy);


        ComboBox<Currencies> curCombo = new ComboBox<>();
        curCombo.getItems().addAll(Currencies.values());
        //Set default selected value
        curCombo.setValue(Currencies.ZAR);

        int row = 0;
        form.add(new Label("Trade ID:"), 0, row);        form.add(tfTradeID, 1, row++);
        tfTradeID.setText("100012345");
        form.add(new Label("Trade Status:"), 0, row);        form.add(tfTradeStatus, 1, row++);
        tfTradeStatus.setText("FO");
        form.add(new Label("Trade Source System:"), 0, row); form.add(tfTradeSourceSystem, 1, row++);
        tfTradeSourceSystem.setText("FO Desk");
        form.add(new Label("Trade Capture Date:"), 0, row); form.add(dpCaptureDate, 1, row++);
        configureDatePicker(dpCaptureDate);
        form.add(new Label("Trader:"), 0, row); form.add(tfUsername, 1, row++);
        tfUsername.setText("Champion");
        form.add(new Label("Portfolio:"), 0, row); form.add(portfoliosCombo, 1, row++);

        form.add(new Label("Counterparty:"), 0, row); form.add(counterpartiesCombo, 1, row++);

        form.add(new Label("Instrument Type:"), 0, row); form.add(tfInstrumentType, 1, row++);
        tfInstrumentType.setText("Overnight Index Swap (OIS)");
        tfInstrumentType.setDisable(true);
        form.add(new Label("Instrument Notes:"), 0, row); form.add(tfInstrumentNotes, 1, row++);
        tfInstrumentNotes.setText("Additional Terms: None");
        form.add(new Label("Number of Units:"), 0, row); form.add(tfUnits, 1, row++);
        form.add(new Label("Buy / Sell:"), 0, row); form.add(buySellCombo, 1, row++);
        form.add(new Label("Base Currency:"), 0, row); form.add(curCombo, 1, row++);
        form.add(new Label("Funding Status:"), 0, row); form.add(tfFundingStatus, 1, row++);
        form.add(new Label("CSA Type:"), 0, row); form.add(tfCSAType, 1, row++);
        form.add(new Label("Risk Definition:"), 0, row); form.add(tfRiskDefinition, 1, row++);

        Map<String, TextField> specificFields = switch (tradeType) {
            case OIS_TRADE -> createOISTradeFields();
            case BOND_TRADE -> createBondTradeFields();
            case LOAN_TRADE -> createLoanTradeFields();
        };

        int col = 2, i = 0;
        for (Map.Entry<String, TextField> entry : specificFields.entrySet()) {
            form.add(new Label(entry.getKey() + ":"), col, i);
            form.add(entry.getValue(), col + 1, i);
            i++;
        }

        Button btnPreview = new Button("Preview");
        btnPreview.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("Trade Type: ").append(tradeType.getLabel()).append("\n\n")
                    .append("Capture Date: ").append(dpCaptureDate.getValue()).append("\n")
                    .append("Username: ").append(tfUsername.getText()).append("\n")
                    //.append("Effective Date: ").append(dpEffectiveDate.getValue()).append("\n")
                    //.append("Maturity Date: ").append(dpMaturityDate.getValue()).append("\n")
                    .append("Counterparty: ").append(tfCounterparty.getText()).append("\n")
                    .append("Portfolio: ").append(tfPortfolio.getText()).append("\n");
            specificFields.forEach((k, v) -> sb.append(k).append(": ").append(v.getText()).append("\n"));

            showInfo("Trade Preview", sb.toString());
        });

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> showInfo("Trade Saved", "Trade ticket for '" + tradeType.getLabel() + "' saved successfully."));

        HBox previewSaveBox = new HBox(10, btnPreview, btnSave);
        previewSaveBox.setStyle("-fx-padding: 10 0 0 0;");
        form.add(previewSaveBox, 0, row++, 4, 1);

        Button btnValuation = new Button("Trade Valuation");
        Button btnFutureCF = new Button("Future Cashflows");
        Button btnRealizedCF = new Button("Realized Cashflows");
        Button btnPnL = new Button("Trade P&L");

        HBox analyticsBox = new HBox(10, btnValuation, btnFutureCF, btnRealizedCF, btnPnL);
        analyticsBox.setStyle("-fx-padding: 10 0 0 0;");
        form.add(analyticsBox, 0, row++, 4, 1);

        InstrumentType instrumentType = new InstrumentType(tfInstrumentType.getText());
        transactionInfo = new TransactionInfo(
                instrumentType,
                tfTradeID.getText(),
                tfTradeStatus.getText(),
                tfTradeSourceSystem.getText(),
                dpCaptureDate.getValue(),
                tfUsername.getText(),
                (portfoliosCombo.getValue()).toString(),
                (counterpartiesCombo.getValue()).toString(),
                tfInstrumentType.getText(),
                tfInstrumentNotes.getText(),
                Double.parseDouble(tfUnits.getText()),
                (buySellCombo.getValue()).toString(),
                (curCombo.getValue()).toString(),
                tfFundingStatus.getText(),
                tfCSAType.getText(),
                tfRiskDefinition.getText());

        List<TradingBook> tradingBooks;
        try {
            tradingBooks = loadTradingBooks("com/ats/tradingsystem/resources/trading_books.json");
            for (TradingBook book : tradingBooks) {
                System.out.println("Loaded TradingBook: " + book);
            }
        } catch (IOException e) {
            System.err.println("Error loading trading books: " + e.getMessage());
        }


        //btnValuation.setOnAction(e -> showInfo("Trade Valuation", "Valuation logic for " + tradeType.getLabel()));
        btnValuation.setOnAction(e -> {

        });

        /*generateButton.setOnAction(e -> {
                    String currency = currencyField.getText();
                    LocalDate maturity = maturityPicker.getValue();
                    double notional = Double.parseDouble(notionalField.getText());
                    double fixedRate = Double.parseDouble(fixedRateField.getText());

                    List<Double> overnightRates = Arrays.asList(new Double[1825]);
                    Collections.fill(overnightRates, 0.00055);
                });*/
        btnFutureCF.setOnAction(e -> showInfo("Future Cashflows", "Future cashflows for " + tradeType.getLabel()));
        btnRealizedCF.setOnAction(e -> showInfo("Realized Cashflows", "Realized cashflows for " + tradeType.getLabel()));
        btnPnL.setOnAction(e -> showInfo("Trade P&L", "P&L analysis for " + tradeType.getLabel()));

        return form;
    }

    private Map<String, TextField> createOISTradeFields() {
        Map<String, TextField> fields = new LinkedHashMap<>();
        DatePicker dpStartDate = new DatePicker(today);
        DatePicker dpMaturityDate = new DatePicker(today.plusYears(5));
        //form.add(new Label("Effective Date:"), 0, row); form.add(dpEffectiveDate, 1, row++);
        //form.add(new Label("Maturity Date:"), 0, row); form.add(dpMaturityDate, 1, row++);
        fields.put("Start Date", new TextField(dateFormatter.format(dpStartDate.getValue())));
        fields.put("Maturity Date", new TextField(dateFormatter.format(dpMaturityDate.getValue())));
        fields.put("Payment Frequency", new TextField("6M"));
        fields.put("Tenor", new TextField("5Y"));
        fields.put("Payment Lag", new TextField("2D"));
        fields.put("Fixing Lag", new TextField("0D"));
        fields.put("Floating Reference Index", new TextField("SOFR"));
        fields.put("Spread", new TextField("0.002"));
        fields.put("Floating Day Count", new TextField((DayCountConventionType.ACTUAL_365).toString()));
        fields.put("FLoating Business Centre", new TextField("JHB"));
        fields.put("Generate Payment Dates from Tenor", new TextField("Yes"));
        fields.put("Roll Direction", new TextField("Backwards"));
        fields.put("Stub Type", new TextField("Short"));
        fields.put("Notional", new TextField("10000000"));
        fields.put("Fixed Rate", new TextField("0.756"));
        fields.put("Fixed Day Count", new TextField((DayCountConventionType.ACTUAL_365).toString()));
        return fields;
    }

    private Map<String, TextField> createBondTradeFields() {
        Map<String, TextField> fields = new LinkedHashMap<>();
        fields.put("Bond Effective Date", new TextField());
        fields.put("Bond Maturity Date", new TextField());
        fields.put("ISIN", new TextField());
        fields.put("Coupon", new TextField());
        fields.put("Face Value", new TextField());
        return fields;
    }

    private Map<String, TextField> createLoanTradeFields() {
        Map<String, TextField> fields = new LinkedHashMap<>();
        fields.put("Loan Effective Date", new TextField());
        fields.put("Loan Maturity Date", new TextField());
        fields.put("Loan Type", new TextField());
        fields.put("Interest Rate", new TextField());
        fields.put("Drawdown Schedule", new TextField());
        return fields;
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public TransactionInfo getTransactionInfo(){
        return this.transactionInfo;
    }
    public static List<TradingBook> loadTradingBooks(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), new TypeReference<List<TradingBook>>() {});
    }

    private void configureDatePicker(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        });
    }
}

