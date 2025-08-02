package com.ats.pricing.foundation.interestRate;

public enum LegType {
    PAYER("Payer"),
    RECEIVER("Receiver");

    private final String description;

    LegType(String description) {
        this.description = description;
    }

    public boolean equalsByDescription(LegType type) {
        return this.description.equals(type.description);
    }

    public static LegType oppositeLeg(LegType legType) {
        return legType == RECEIVER ? PAYER : RECEIVER;
    }

    @Override
    public String toString() {
        return description;
    }
}

