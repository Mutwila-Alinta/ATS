package com.ats.pricing.foundation;

public final class Payment{ //implements Serializable, ThreadSafe, Comparable<Payment>, Cloneable {

   // private static final long serialVersionUID = 6810509454090377447L;


    private String paymentUuid;

    private static final String DEFAULT_PATTERN = "###,###.##";


    private String description;

    private Currency currency;

    private double amount;

    protected Payment(Double amount, Currency currency, String paymentUuid) {
        this.amount = amount;
        this.currency = currency;
        this.paymentUuid = paymentUuid;
    }



    public Payment(double amount, Currency currency, String description) {
        this.amount = amount;
        this.currency = currency;
        this.description = description;
    }

    /**
     * Creates a payment from another payment.
     */
    public Payment(Payment payment) {
        this.amount = payment.amount;
        this.currency = payment.currency;
        this.description = payment.description;
    }

    @SuppressWarnings("unused")
    private Payment() {
    }

    /**
     * Returns the amount.
     */

    public final double getAmount() {
        return amount;
    }

    /**
     * Returns the currency.
     */

    public final Currency getCurrency() {
        return currency;
    }

    /**
     * Returns the description.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Returns true if the argument is a <code>Payment</code> for the same amount in
     * same currency.
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Payment) {
            Payment arg = (Payment) object;
            if (amount != arg.amount) {
                return false;
            }
            if (!currency.equals(arg.currency)) {
                return false;
            }
            return (description == null) ? (arg.description == null) : description.equals(arg.description);
        }
        return false;
    }

    public int hashCode() {
        return currency.hashCode() + Double.valueOf(amount).hashCode();
    }




    public Payment round(int precision) {
        long factor = (long) Math.pow(10, precision);
        double roundedAmount = (double) Math.round(amount * factor) / factor;
        return new Payment(roundedAmount, currency, description);
    }

    /**
     * Multiply the payment by a factor.
     */

    public Payment multiplyBy( double x) {
        if (x == 1.0) {
            return this;
        }
        return new Payment(amount * x, currency, description);
    }

    public Payment multiplyBy(Payment multiplier) {
        if (!currency.equals(multiplier.getCurrency())) {
            throw new IllegalArgumentException("Cannot multiply two payments with different currencies: " + currency
                    + "!=" + multiplier.getCurrency());
        }
        return multiplyBy(multiplier.getAmount());
    }

    /**
     * Divide the payment by a factor.
     */

    public Payment divideBy( double x) {
        if (x == 1.0) {
            return this;
        }
        return new Payment(amount / x, currency, description);
    }
    public Payment add(double amount) {
        return new Payment(this.amount + amount, currency, description);
    }

    public Payment subtract(double amount) {
        return new Payment(this.amount - amount, currency, description);
    }

    @Override
    public Payment clone() {
        try {
            Payment c = (Payment) super.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int compareTo(Payment obj) {
        if (obj == this) {
            return 0;
        }
        if (obj == null) {
            return 1;
        }
        Payment pmt = obj;
        int test = getCurrency().compareTo(pmt.getCurrency());
        if (test != 0) {
            return test;
        }
        return Double.valueOf(this.getAmount()).compareTo(pmt.getAmount());
    }

    /**
     * Returns a new payment with the required description.The amount and currency
     * are the same as this instance.
     */
    public Payment setDescription(String description) {
        return new Payment(amount, currency, description);
    }

    public Payment setAmount(double amount) {
        return new Payment(amount, currency, description);
    }

}
