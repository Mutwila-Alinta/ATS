package com.ats.pricing.foundation;

import com.ats.pricing.instruments.InstrumentType;
import java.time.LocalDate;

public final class TransactionInfo //implements Serializable
{
    //static final long serialVersionUID = -3862538220796158364L;
    private InstrumentType instrumentType;
    private String tfTradeID;
    private String tfTradeStatus;
    private String tfTradeSourceSystem;
    private LocalDate dpCaptureDate;
    private String tfUsername;
    private String portfoliosCombo;
    private String counterpartiesCombo;
    private String tfInstrumentType;
    private String tfInstrumentNotes;
    //Counterparty ;
    private double tfUnits;
    private String buySellCombo;
    private String curCombo;
    //String specialInfo1; String specialInfo2;
    private String tfFundingStatus;
    private String tfCSAType;
    private String tfRiskDefinition;


    public TransactionInfo() {

    }

    public TransactionInfo(InstrumentType instrumentType,
                           String tfTradeID,
                           String tfTradeStatus,
                           String tfTradeSourceSystem,
                           LocalDate dpCaptureDate,
                           String tfUsername,
                           String portfoliosCombo,
                           String counterpartiesCombo,
                           String tfInstrumentType,
                           String tfInstrumentNotes,
                           //Counterparty ,
                           double tfUnits,
                           String buySellCombo,
                           String curCombo,
                           //String specialInfo1, String specialInfo2,
                           String tfFundingStatus,
                           String tfCSAType,
                           String tfRiskDefinition) {
        super();
        this.instrumentType = instrumentType;
        this.tfTradeID = tfTradeID;
        this.tfTradeStatus = tfTradeStatus;
        this.tfTradeSourceSystem = tfTradeSourceSystem;
        this.dpCaptureDate = dpCaptureDate;
        this.tfUsername = tfUsername;
        this.portfoliosCombo = portfoliosCombo;
        this.counterpartiesCombo = counterpartiesCombo;
        this.tfInstrumentType = tfInstrumentType;
        this.tfInstrumentNotes = tfInstrumentNotes;
        //Counterparty =
        this.tfUnits = tfUnits;
        this.buySellCombo = buySellCombo;
        this.curCombo = curCombo;
        //String specialInfo1, String specialInfo2,
        this.tfFundingStatus = tfFundingStatus;
        this.tfCSAType = tfCSAType;
        this.tfRiskDefinition = tfRiskDefinition;
    }


    /**
     * Returns a transaction information relevant to internal risk calculations.
     * This is a subset of the complete transaction information used by trader.
     */
        /*ublic static TransactionInfo getInternalInstance(LocalDate transactionDate, double numberOfUnits, String book) {
            return new TransactionInfo(null, null, "", "", "", transactionDate, transactionDate, null, null, book, null,
                    numberOfUnits, "", "", "", 
                    //FundingStatus.UNFUNDED, RiskDefinition.TRADING, 
                    //null, null, null, 
                    //null,
                    null);
        }*/

       /* public static TransactionInfo newInstance(TransactionInfo orig) {
            TransactionInfo instance = new TransactionInfo(orig.getInstrumentType(), orig.getTradeReference(),
                    orig.getTransactionUuid(), orig.getTransactionReference(), orig.getTransactor(),
                    orig.getTransactionDate(), orig.getSystemDate(), orig.getReissuedDate(), orig.getReissuedReference(),
                    orig.getBook(), orig.getCounterparty(), orig.getNumberOfUnits(), orig.getStatus(),
                    orig.getSpecialInfo1(), orig.getSpecialInfo2(), orig.getFundingStatus(), orig.getRiskDefinition(),
                    orig.getSystemReference(), orig.getCsa(), orig.getContractSubType(), orig.getProductUsage(),
                    orig.getDataExportInfo());

            return instance;
        }*/





    /**
     * Returns the InstrumentType.
     *
     * @return the InstrumentType.
     */
    public final InstrumentType getInstrumentType() {
        return instrumentType;
    }


    // Method will be replaced, just temp.
        /*public Csa getCsa() {
            if (this.csa == null) {
                return NoneCSA.newNoneCSAInstance();
            }
            return this.csa;
        }

        public void setCsa(Csa csa) {
            this.csa = csa;
        }*/





    /**
     * Returns the terminationDate.
     * <p>
     * <p>
     * <p>


    /**
     * Returns the transactor.
     *
     * @return the transactor.
     */
    public final String getTransactor() {
        return"";// transactor;
    }

       /* public final RiskDefinition getRiskDefinition() {
            return riskDefinition;
        }

        public final SystemReference getSystemReference() {
            return systemReference;
        }

        public void setContractSubType(ContractSubType contractSubType) {
            this.contractSubType = contractSubType;
        }*/





    public void setInstrumentType(InstrumentType instrumentType) {
                        this.instrumentType=instrumentType;
       }

    public final String getTradeID() {
        return tfTradeID;
    }
        public void setTradeID(String tfTradeID){
            this.tfTradeID=tfTradeID;
}

    public final String getTradeStatus() {
        return tfTradeStatus;
    }
    public void setTradeStatus(String tfTradeStatus) {
        this.tfTradeStatus = tfTradeStatus;
    }
    public final String getTradeSourceSystem() {
        return tfTradeSourceSystem;
    }
        public void setTradeSourceSystem(String tfTradeSourceSystem) {
            this.tfTradeSourceSystem = tfTradeSourceSystem;
        }
    public final LocalDate getCaptureDate() {
        return dpCaptureDate;
    }
        public void seCaptureDate(LocalDate dpCaptureDate) {
            this.dpCaptureDate = dpCaptureDate;
        }
    public final String getUsername() {
        return tfUsername;
    }
        public void setUsername(String tfUsername) {
            this.tfUsername = tfUsername;
        }
    public final String getPortfolio() {
        return portfoliosCombo;
    }
        public void setPortfolios(String portfoliosCombo) {
            this.portfoliosCombo = portfoliosCombo;
        }
    public final String getCounterparty() {
        return counterpartiesCombo;
    }
        public void setCounterparty(String counterpartiesCombo) {
            this.counterpartiesCombo = counterpartiesCombo;
        }

    public final String getInstrumentType2() {
        return tfInstrumentType;
    }
        public void setInstrumentType2(String tfInstrumentType) {
            this.tfInstrumentType = tfInstrumentType;
        }
    public final String getInstrumentNotes() {
        return tfInstrumentNotes;
    }
        public void setInstrumentNotes(String tfInstrumentNotes) {
            this.tfInstrumentNotes = tfInstrumentNotes;
        }
    public final double getUnits() {
        return tfUnits;
    }
        public void setUnits(double tfUnits) {
            //Counterparty =
            this.tfUnits = tfUnits;
        }

    public final String getBuySell() {
        return buySellCombo;
    }
        public void setBuySell(String buySellCombo) {
            this.buySellCombo = buySellCombo;
        }

    public final String getCurrency() {
        return curCombo;
    }
        public void setCurrency(String curCombo) {
            this.curCombo = curCombo;
        }
    public final String getFundingStatus() {
        return tfFundingStatus;
    }
        //public void setSpecialInfo2(
        //String specialInfo1, String specialInfo2,
        public void setFundingStatus(String tfFundingStatus) {
            this.tfFundingStatus = tfFundingStatus;
        }

    public final String getCSAType() {
        return tfCSAType;
    }

        public void setCSAType(String tfCSAType){
            this.tfCSAType=tfCSAType;
    }

    public final String getRiskDefinition() {
        return tfRiskDefinition;
    }
        public void setRiskDefinition(String tfRiskDefinition){
            this.tfRiskDefinition=tfRiskDefinition;
}





       /* @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(super.toString());
            buffer.append(" [InstrumentType=" + instrumentType.getSpecificType());
            buffer.append(", tradeReference=" + tradeReference);
            buffer.append(", transactionUuid=" + transactionUuid);
            buffer.append(", transactionReference=" + transactionReference);
            buffer.append(", transactor=" + transactor);
            buffer.append(", transactionDate=" + transactionDate);
            buffer.append(", systemDate=" + systemDate);
            buffer.append(", reissuedDate=" + reissuedDate);
            buffer.append(", reissuedReference=" + reissuedReference);
            buffer.append(", book=" + book);
            buffer.append(", counterparty=" + counterparty);
            buffer.append(", numberOfUnits=" + numberOfUnits);
            buffer.append(", status=" + status);
            buffer.append(", specialInfo1=" + specialInfo1);
            buffer.append(", specialInfo2=" + specialInfo2 + "]");

            buffer.append(", product usage=" + productUsage);

            return buffer.toString();
        }*/





       /* public String getProductUsage() {
            return productUsage;
        }

        public void setProductUsage(String productUsage) {
            this.productUsage = productUsage;
        }*/
    }
