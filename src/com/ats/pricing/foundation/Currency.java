package com.ats.pricing.foundation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public final class Currency{ //extends ReportableCurrency implements Serializable, ThreadSafe, Comparable<Currency>, QuantToolsInstance<QuantToolsCurrency> {

    //private static final long serialVersionUID = -5415295649487547786L;

    private static HashMap<String, Currency> instances;

    static {
        initialize();
    }
    private static void initialize() {
        Set<java.util.Currency> standardCurrencies = java.util.Currency.getAvailableCurrencies();
        Currency[] nonStandards = new Currency[] {
                new Currency(MajorCurrencies.EUR_JHB_CODE, 2, MajorCurrencies.EUR_JHB_CODE),
                new Currency(MajorCurrencies.CNH_CODE, 2, MajorCurrencies.CNH_CODE),

                new Currency(MajorCurrencies.EGP_CODE, 2, MajorCurrencies.EGP_CODE), new Currency(MajorCurrencies.AOA_CODE, 2, MajorCurrencies.AOA_CODE) };
        instances = new HashMap<String, Currency>((int) ((standardCurrencies.size() + nonStandards.length) / 0.75 + 1));

        for (java.util.Currency currency : java.util.Currency.getAvailableCurrencies()) {
            instances.put(currency.getCurrencyCode(), new Currency(currency));
        }

        for (Currency currency : nonStandards) {
            Currency existing = instances.put(currency.getCurrencyCode(), currency);
            if (existing != null) {
                instances.put(existing.getCurrencyCode(), existing);
            }
        }
    }


    private String currencyCode;

    private transient java.util.Currency currency;

    private transient int defaultFractionDigits;

    private transient String symbol;



    public Currency() {
    }

    /**
     * Creates an instance of <code>Currency</code> that encapsulates its
     * corresponding sdk counterpart.
     *
     * @param currency the sdk currency to encapsulate.
     */
    private Currency(java.util.Currency currency) {
        this.currency = currency;
        this.currencyCode = currency.getCurrencyCode();
    }

    /**
     * Creates an instance of <code>Currency</code> that encapsulates its code,
     * symbol and defaultFractionDigits.
     */
    private Currency(String currencyCode, int defaultFractionDigits, String symbol) {
        this.currencyCode = currencyCode;
        this.defaultFractionDigits = defaultFractionDigits;
        this.symbol = symbol;
    }



    @Override
    public int hashCode() {
        return currencyCode.hashCode();
    }

    /*@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Currency other = (Currency) obj;
        if (currencyCode == null) {
            if (other.currencyCode != null)
                return false;
        } else if (!currencyCode.equals(other.currencyCode))
            return false;
        return true;
    }
*/
    public static Currency getDefault() {
        return instances.get(MajorCurrencies.ZAR_CODE);
    }


    /**
     * @return currency code of this currency instance.
     */

    public String getCurrencyCode() {
        return currencyCode;
    }


    public int compareTo(Currency o) {
        if (o == this) {
            return 0;
        }
        return this.getCurrencyCode().compareTo((o).getCurrencyCode());
    }

    public Object getMBeanValue() {
        return this.getCurrencyCode();
    }

    @Override
    public String toString() {
        return currencyCode;
    }

    public int getDefaultFractionDigits() {
        if (currency != null) {
            return currency.getDefaultFractionDigits();
        }
        return defaultFractionDigits;
    }

    public String getSymbol() {
        if (currency != null) {
            return currency.getSymbol();
        }
        return symbol;
    }

    public String getSymbol(Locale locale) {
        if (currency != null) {
            return currency.getSymbol(locale);
        }
        return getSymbol();
    }

    public Currency getCurrency() {
        return this;
    }
}
