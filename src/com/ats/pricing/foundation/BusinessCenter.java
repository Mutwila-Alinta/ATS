package com.ats.pricing.foundation;

import com.ats.pricing.enums.BusinessCenterEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Currency;
import java.util.Objects;
import java.util.Set;
import java.util.SimpleTimeZone;

public final class BusinessCenter implements Comparable<BusinessCenter>, Cloneable {

    public static final String BUSINESS_CENTER_PROPERTY = "businessCenter";

    private String code;
    private String description;
    private Currency defaultCurrency;
    private Integer version;
    private String uuid;

    // Constructors
    protected BusinessCenter() {
    }

    protected BusinessCenter(String code) {
        this.code = code;
    }

    public BusinessCenter(String uuid, String code, String description, String zoneId,
                          Currency defaultCurrency, Integer version) {
        this.uuid = uuid;
        this.code = code;
        this.description = description;
        this.defaultCurrency = defaultCurrency;
        this.version = version;
    }

    public static BusinessCenter getInstance(String code) {
        BusinessCenter bc = BusinessCenterEnum.getInstance(code);
        if (bc == null) {
            throw new IllegalArgumentException(code + " is not a valid business center code.");
        }
        return bc;
    }
    // Static Factory Methods
    BusinessCenter bc = BusinessCenterEnum.getInstance(code);

    public static BusinessCenter getDefault() {
        return getInstance("ZAJO");
    }


    public static BusinessCenter newInstance(String code) {
        return getInstance(code);
    }

    public BusinessDayCalendar getBusinessDayCalendar() {
        return BusinessDayCalendar.getInstance(code);
    }

    // Getters
    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public Currency getCurrency() {
        return getDefaultCurrency();
    }

    public Integer getVersion() {
        return version;
    }

    public String getUuid() {
        return uuid;
    }


    // Setters
    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    // Overrides
    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BusinessCenter other) && Objects.equals(this.code, other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public int compareTo(BusinessCenter other) {
        return this.code.compareTo(other.code);
    }

    public Object getMBeanValue() {
        return getCode();
    }

    @Override
    public Object clone() {
        try {
            return super.clone(); // shallow is fine; class is mostly immutable
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

}
