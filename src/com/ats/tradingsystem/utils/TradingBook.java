package com.ats.tradingsystem.utils;

import java.io.Serializable;
import java.util.Objects;

public class TradingBook implements Comparable<TradingBook>, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private int version;
    private String tradingBookCode;
    private String name;
    private boolean enabled;
    private boolean restricted;

    // Constructor
    public TradingBook(String id, String tradingBookCode, String name, boolean enabled, boolean restricted, int version) {
        if (id == null || tradingBookCode == null || name == null) {
            throw new IllegalArgumentException("TradingBook ID, code, and name cannot be null");
        }
        this.id = id;
        this.tradingBookCode = tradingBookCode;
        this.name = name;
        this.enabled = enabled;
        this.restricted = restricted;
        this.version = version;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public String getTradingBookCode() {
        return tradingBookCode;
    }

    protected void setTradingBookCode(String tradingBookCode) {
        if (tradingBookCode == null) {
            throw new IllegalArgumentException("TradingBook code cannot be null");
        }
        this.tradingBookCode = tradingBookCode;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("TradingBook name cannot be null");
        }
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isRestricted() {
        return restricted;
    }

    protected void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isLeafTradingBook() {
        return false;
    }

    @Override
    public int compareTo(TradingBook other) {
        if (other == null) return 1;
        return this.tradingBookCode.compareTo(other.tradingBookCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TradingBook)) return false;
        TradingBook other = (TradingBook) obj;
        return Objects.equals(this.tradingBookCode, other.tradingBookCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradingBookCode);
    }

    @Override
    public String toString() {
        return tradingBookCode;
    }
}
