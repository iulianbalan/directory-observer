package com.advicer.monitor.exceptions;

/**
 * Custom Exception
 *
 * @author Iulian Balan
 */
public class DirectoryAccessException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param msg error message
     */
    public DirectoryAccessException(String msg) {
        super(msg);
    }

    /**
     * Exception with no error message
     */
    public DirectoryAccessException() {
        super();
    }

}
