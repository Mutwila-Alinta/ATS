package com.ats.tradingsystem.enums;

public enum BuySell {
    Buy("Buy"),
    Sell("Sell");

    private final String label;
    BuySell(String label) { this.label = label; }
    public String getLabel() { return label; }

    public static BuySell fromLabel(String label) {
        for (BuySell type : values()) {
            if (type.getLabel().equals(label)) return type;
        }
        return null;
    }
}
