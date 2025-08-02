package com.ats.pricing.instruments;

public class InstrumentType {


private String specificType;



public InstrumentType() {
}

public InstrumentType(String specificType) {
    this.setSpecificType(specificType);
}
    /**
     * @return the specificType
     */
    public final String getSpecificType() {
        return specificType;
    }

    /**
     * @param specificType the specificType to set
     */
    private void setSpecificType(String specificType) {
        if (specificType == null) {
            throw new IllegalArgumentException("Cannot set specificType to null");
        }
        this.specificType = specificType;
    }
}