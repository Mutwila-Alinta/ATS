package com.ats.pricing.instrumentsFactory;

import com.ats.pricing.foundation.TransactionInfo;
import com.ats.pricing.instruments.OvernightIndexSwap;

public class OvernightIndexSwapFactory implements InstrumentFactoryGUIBase {

    //private static Log log = LogFactory.getLog(OvernightIndexSwapFactory.class);

    private static final String genericType = "instrument";
    private static final String specificType = "overnightIndexSwap";
    private static final Class<OvernightIndexSwap> clacc = OvernightIndexSwap.class;

    private TransactionInfo transactionInfo = null;
    public OvernightIndexSwapFactory( TransactionInfo transactionInfo){ //throws Exception {
        this.transactionInfo =transactionInfo ;
    }

    public String getGenericType() {
        return genericType;
    }
    public String getSpecificType() {
        return specificType;
    }
    public Class getObjectClass() {
        return clacc;
    }


    //@Override
    public Object getInstrumentInstance2( TransactionInfo transactionInfo){
        this.transactionInfo=transactionInfo;
        return transactionInfo;
    }



    //OvernightIndexSwap contract = new OvernightIndexSwap(transactionInfo);
        /*contract.decanonicalize(canonicalMBean);
        boolean isReceiveStreamCustom = contract.isReceiveStreamCustom();
        if (!isReceiveStreamCustom) {

            contract.initialize(
                    InterestStreamFactory.getCapitalStream(contract.getNotional(), contract.isNotionalExchange(),
                            contract.isReceiveStreamCustom(), mbean, NotionalExchangeStrategy.DEFAULT, false));
        }
        contract.validateSpecificProperties(isReceiveStreamCustom);
        return contract;*/


   /* @Override
    public void display() {
        Stage stage = new Stage();

        TextField currencyField = new TextField("USD");
        DatePicker maturityPicker = new DatePicker(LocalDate.now().plusYears(5));
        TextField notionalField = new TextField("10000000");
        TextField fixedRateField = new TextField("0.0005");

        Button generateButton = new Button("Create and Value OIS");

        VBox layout = new VBox(10, new Label("Currency:"), currencyField,
                new Label("Maturity Date:"), maturityPicker,
                new Label("Notional:"), notionalField,
                new Label("Fixed Rate:"), fixedRateField,
                generateButton);
        layout.setPadding(new javafx.geometry.Insets(20));

        generateButton.setOnAction(e -> {
            String currency = currencyField.getText();
            LocalDate maturity = maturityPicker.getValue();
            double notional = Double.parseDouble(notionalField.getText());
            double fixedRate = Double.parseDouble(fixedRateField.getText());

            List<Double> overnightRates = Arrays.asList(new Double[1825]);
            Collections.fill(overnightRates, 0.00055);

            Map<LocalDate, Double> discountMap = DiscountCurveBuilder.buildFlatCurve(maturity, 0.01);
            ZeroCurve zeroCurve = new ZeroCurve(discountMap);

            OIS ois = OISInstrumentFactory.createOIS(currency, maturity, notional, fixedRate, overnightRates);
            OISValuationEngine engine = new OISValuationEngine(zeroCurve);

            double pv = engine.value(ois);
            List<Cashflow> cashflows = engine.generateCashflows(ois);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("OIS Valuation");
            alert.setHeaderText("Present Value: " + pv);
            alert.setContentText("Cashflows:\n" + cashflows.toString());
            alert.showAndWait();
        });

        stage.setTitle("OIS Factory GUI");
        stage.setScene(new Scene(layout));
        stage.show();
    }*/

    @Override
    public Object getInstrumentInstance(TransactionInfo info) {
        OvernightIndexSwap contract = new OvernightIndexSwap(transactionInfo);
        //contract.validateSpecificProperties(isReceiveStreamCustom);
        return contract;
    }
}
