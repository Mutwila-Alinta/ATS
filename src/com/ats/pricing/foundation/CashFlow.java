package com.ats.pricing.foundation;

import com.ats.pricing.foundation.interestRate.LegType;
import com.ats.tradingsystem.utils.exceptions.MarketDataException;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDate;
import java.util.Objects;

public final class CashFlow implements Comparable<CashFlow> {

    private final String cashFlowUuid;
    private final Payment payment;
    private final LocalDate date;
    private final String cashFlowType;
    private final LegType legType;
    private final String id;
    private final String legName;
    private final String legId;
    private final String constituentType;
    private final boolean isPrincipal;

    public CashFlow(Payment payment, LocalDate date) {
        this(payment, date, "", null, null, null, null, false, null);
    }

    public CashFlow(Payment payment, LocalDate date, String cashFlowType) {
        this(payment, date, cashFlowType, null, null, null, null, false, null);
    }

    public CashFlow(Payment payment, LocalDate date, String cashFlowType, LegType legType) {
        this(payment, date, cashFlowType, legType, null, null, null, false, null);
    }

    public CashFlow(Payment payment, LocalDate date, String cashFlowType, LegType legType,
                    String id, String legName, boolean isPrincipal) {
        this(payment, date, cashFlowType, legType, id, legName, null, isPrincipal, null);
    }

    public CashFlow(Payment payment, LocalDate date, String cashFlowType, LegType legType,
                    String id, String legName, String legId, boolean isPrincipal, String cashFlowUuid) {
        this.payment = Objects.requireNonNull(payment);
        this.date = Objects.requireNonNull(date);
        this.cashFlowType = cashFlowType != null ? cashFlowType : "";
        this.legType = legType;
        this.id = id;
        this.legName = legName;
        this.legId = legId;
        this.constituentType = null;
        this.isPrincipal = isPrincipal;
        this.cashFlowUuid = cashFlowUuid;
    }

    public Payment getPayment() {
        return payment;
    }

    public double getPaymentAmount() {
        return payment.getAmount();
    }

    public Currency getCurrency() {
        return payment.getCurrency();
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCashFlowType() {
        return cashFlowType;
    }

    public LegType getLegType() {
        return legType;
    }

    public String getId() {
        return id;
    }

    public String getLegId() {
        return legId;
    }

    public String getLegName() {
        return legName;
    }

    public String getConstituentType() {
        return constituentType;
    }

    public boolean isPrincipal() {
        return isPrincipal;
    }

    public String getCashFlowUuid() {
        return cashFlowUuid;
    }

    public CashFlow multiplyBy(double factor) {
        return new CashFlow(payment.multiplyBy(factor), date, cashFlowType, legType, id, legName, legId, isPrincipal, cashFlowUuid);
    }

    public CashFlow divideBy(double factor) {
        return new CashFlow(payment.divideBy(factor), date, cashFlowType, legType, id, legName, legId, isPrincipal, cashFlowUuid);
    }

    public CashFlow add(Payment p) {
        return new CashFlow(payment.add(p.getAmount()), date, cashFlowType, legType, id, legName, legId, isPrincipal, cashFlowUuid);
    }

    public CashFlow subtract(Payment p) {
        return new CashFlow(payment.subtract(p.getAmount()), date, cashFlowType, legType, id, legName, legId, isPrincipal, cashFlowUuid);
    }

    public Payment discount(LocalDate discountDate, InterestRateSource discountingSource) throws MarketDataException {
        if (discountDate.isEqual(date)) {
            return payment;
        }
        if (discountDate.isAfter(date)) {
            throw new MarketDataException("Cannot discount to a future date: " + discountDate + " is after " + date);
        }
        double discountFactor = discountingSource.getDiscountFactor(discountDate, date);
        return payment.multiplyBy(discountFactor);
    }

    public Payment discount(LocalDate discountDate, InterestRate interestRate) throws MarketDataException {
        if (discountDate.isEqual(date)) {
            return payment;
        }
        if (discountDate.isAfter(date)) {
            throw new MarketDataException("Cannot discount to a future date: " + discountDate + " is after " + date);
        }
        double discountFactor = interestRate.getDiscountFactor(discountDate, date);
        return payment.multiplyBy(discountFactor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashFlow other)) return false;
        return Objects.equals(date, other.date) && Objects.equals(payment, other.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, payment);
    }

    @Override
    public int compareTo(CashFlow other) {
        int cmp = date.compareTo(other.date);
        return (cmp != 0) ? cmp : payment.compareTo(other.payment);
    }

    @Override
    public String toString() {
        return payment + " on " + date;
    }
}
