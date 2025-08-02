package com.ats.tradingsystem.enums;

public enum TradeType {
    OIS_TRADE("OIS Trade"),
    BOND_TRADE("Bond Trade"),
    LOAN_TRADE("Loan Trade");

    private final String label;
    TradeType(String label) { this.label = label; }
    public String getLabel() { return label; }

    public static TradeType fromLabel(String label) {
        for (TradeType type : values()) {
            if (type.getLabel().equals(label)) return type;
        }
        return null;
    }
}