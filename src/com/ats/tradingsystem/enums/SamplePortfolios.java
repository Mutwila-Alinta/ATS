package com.ats.tradingsystem.enums;

public enum SamplePortfolios {
    Book1("Interest Payment Book"),
    Book2("Loans Book"),
    Book3("High Net Book");

    private final String label;
    SamplePortfolios(String label) { this.label = label; }
    public String getLabel() { return label; }

    public static SamplePortfolios fromLabel(String label) {
        for (SamplePortfolios book : values()) {
            if (book.getLabel().equals(label)) return book;
        }
        return null;
    }
}
