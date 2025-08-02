package com.ats.tradingsystem.utils.exceptions;

/**
 * Represents a MeasureException caused by incorrect or missing market data.
 *
 */
public class MarketDataException extends MeasureException {

    static final long serialVersionUID = 6864177845637726099L;

    public MarketDataException() {
        super();
    }

    public MarketDataException(String message) {
        super(message);
    }

    public MarketDataException(Throwable cause) {
        super(cause);
    }

    public MarketDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
