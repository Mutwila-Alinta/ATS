package com.ats.tradingsystem.utils;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Counterparty implements Comparable<Counterparty> {

    private String counterpartyId;
    private String counterpartyCode;
    private String name;
    private boolean enabled;
    private boolean bank;

    private String cifReference;
    private String osdNumber;
    private String country;
    private String rating;
    private String sector;
    private String location;
    private String locationId;

    private boolean resident;
    private boolean standardBank;

    private Set<TradingBook> tradingBooks = new TreeSet<>();
    private Set<String> tradingBookIds = new TreeSet<>();
    private Set<CounterpartyRole> roles = new TreeSet<>();

    protected Counterparty() {
        // For persistence or JSON deserialization
    }

    public Counterparty(String counterpartyId, String counterpartyCode, String name, boolean enabled,
                        String cifReference, String osdNumber, String country, String location,
                        Set<TradingBook> tradingBooks, Set<CounterpartyRole> roles,
                        String rating, String sector, boolean bank) {
        this.counterpartyId = counterpartyId;
        this.counterpartyCode = counterpartyCode;
        this.name = name;
        this.enabled = enabled;
        this.cifReference = cifReference;
        this.osdNumber = osdNumber;
        this.country = country;
        this.location = location;
        this.locationId = location;  // Assuming locationId is same as location (if not, adjust accordingly)
        this.tradingBooks = tradingBooks != null ? tradingBooks : new TreeSet<>();
        this.roles = roles != null ? roles : new TreeSet<>();
        this.rating = rating;
        this.sector = sector;
        this.bank = bank;
    }

    // Basic Getters & Setters
    public String getCounterpartyId() {
        return counterpartyId;
    }

    public String getCounterpartyCode() {
        return counterpartyCode;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getCifReference() {
        return cifReference;
    }

    public String getOsdNumber() {
        return osdNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getRating() {
        return rating;
    }

    public String getSector() {
        return sector;
    }

    public boolean isBank() {
        return bank;
    }

    public boolean isResident() {
        return resident;
    }

    public void setResident(boolean resident) {
        this.resident = resident;
    }

    public boolean isStandardBank() {
        return standardBank;
    }

    public void setStandardBank(boolean standardBank) {
        this.standardBank = standardBank;
    }

    public String getLocation() {
        return location;
    }

    @JsonIgnore
    public String getLocationId() {
        return locationId;
    }

    // Roles Management
    public Set<CounterpartyRole> getRoles() {
        return roles;
    }

    public void addRole(CounterpartyRole role) {
        if (role != null) {
            this.roles.add(role);
        }
    }

    public void addRoles(Collection<CounterpartyRole> roles) {
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public void removeRoles(Collection<CounterpartyRole> roles) {
        if (roles != null) {
            this.roles.removeAll(roles);
        }
    }

    // TradingBook Management
    @JsonIgnore
    public Set<TradingBook> getTradingBooks() {
        return tradingBooks;
    }

    public void addTradingBook(TradingBook book) {
        if (book != null) {
            this.tradingBooks.add(book);
            this.tradingBookIds.add(book.getTradingBookCode());
        }
    }

    public void addTradingBooks(Collection<TradingBook> books) {
        if (books != null) {
            for (TradingBook book : books) {
                addTradingBook(book);
            }
        }
    }

    public void removeTradingBook(TradingBook book) {
        if (book != null) {
            this.tradingBooks.remove(book);
            this.tradingBookIds.remove(book.getTradingBookCode());
        }
    }

    public Set<String> getTradingBookIds() {
        return tradingBookIds;
    }

    public void setTradingBookIds(Set<String> tradingBookIds) {
        this.tradingBookIds = tradingBookIds;
    }

    // Comparable & Equality
    @Override
    public int compareTo(Counterparty other) {
        if (other == this) return 0;
        if (other == null) return 1;
        int cmp = this.getLocationId().compareTo(other.getLocationId());
        if (cmp != 0) return cmp;
        return this.getName().compareTo(other.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Counterparty)) return false;
        Counterparty other = (Counterparty) obj;
        return Objects.equals(this.counterpartyCode, other.counterpartyCode) &&
                Objects.equals(this.locationId, other.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterpartyCode, locationId);
    }

    @Override
    public String toString() {
        return counterpartyCode;
    }
}
