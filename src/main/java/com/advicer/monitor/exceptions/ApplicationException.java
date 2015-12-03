package com.advicer.monitor.exceptions;

/**
 * Custom Exception
 *
 * @author Iulian Balan
 */
public class ApplicationException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param msg error message
     */
    public ApplicationException(String msg, Throwable e) {
        super(msg, e);
    }

}
