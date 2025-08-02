package com.ats.pricing.enums;

import com.ats.pricing.foundation.BusinessCenter;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public enum BusinessCenterEnum {

    ZAJO(new BusinessCenter("uuid-zajo", "ZAJO", "Johannesburg", "Africa/Johannesburg", Currency.getInstance("ZAR"), 1)),
    USNY(new BusinessCenter("uuid-usny", "USNY", "New York", "America/New_York", Currency.getInstance("USD"), 1)),
    GBLO(new BusinessCenter("uuid-gblo", "GBLO", "London", "Europe/London", Currency.getInstance("GBP"), 1));
    // Add more entries as needed

    private final BusinessCenter businessCenter;

    BusinessCenterEnum(BusinessCenter businessCenter) {
        this.businessCenter = businessCenter;
    }

    public BusinessCenter getBusinessCenter() {
        return businessCenter;
    }

    private static final Map<String, BusinessCenter> LOOKUP_MAP = new HashMap<>();

    static {
        for (BusinessCenterEnum e : values()) {
            LOOKUP_MAP.put(e.businessCenter.getCode(), e.businessCenter);
        }
    }

    public static BusinessCenter getInstance(String code) {
        return LOOKUP_MAP.get(code);
    }
}
