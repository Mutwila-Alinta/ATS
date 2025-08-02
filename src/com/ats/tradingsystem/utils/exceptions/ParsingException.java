package com.ats.tradingsystem.utils.exceptions;

/**
 * Indicates that a value cannot be parsed to a meaningful Java object.
 */
public class ParsingException extends Exception {

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

