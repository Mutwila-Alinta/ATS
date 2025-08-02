package com.ats.tradingsystem.enums;

public enum SampleCounterparties {
    Voda("Vodacom Test Cpty"),
    MtN("MTN Test Cpty"),
    STD_Bank("STD Bank Test Cpty");

    private final String label;
    SampleCounterparties(String label) { this.label = label; }
    public String getLabel() { return label; }

    public static SampleCounterparties fromLabel(String label) {
        for (SampleCounterparties cpty : values()) {
            if (cpty.getLabel().equals(label)) return cpty;
        }
        return null;
    }
}
