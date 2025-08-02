package com.ats.tradingsystem.utils.exceptions;

/**
 * Represents "user defined" exceptions thrown by <code>measure</code> centric
 * methods. It "wraps" the actual "user defined" exception thrown. Does not
 * include the runtime exceptions.
 */
public class MeasureException extends Exception {


    /**
     * Default constructor.
     */
    public MeasureException() {
        super();
    }

    public MeasureException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeasureException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor that allows a specific error message to be specified.
     *
     * @param message
     *            the detail message.
     */
    public MeasureException(String message) {
        super(message);
    }
}