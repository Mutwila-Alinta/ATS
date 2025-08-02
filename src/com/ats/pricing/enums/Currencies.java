package com.ats.pricing.enums;

public enum Currencies {
    USD("USD"),
    ZAR("ZAR"),
    EUR("EUR");

    private final String label;
    Currencies(String label) { this.label = label; }
    public String getLabel() { return label; }

    public static Currencies fromLabel(String label) {
        for (Currencies cpty : values()) {
            if (cpty.getLabel().equals(label)) return cpty;
        }
        return null;
    }
}