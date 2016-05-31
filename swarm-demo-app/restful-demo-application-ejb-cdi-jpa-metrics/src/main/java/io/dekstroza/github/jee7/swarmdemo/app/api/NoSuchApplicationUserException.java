package io.dekstroza.github.jee7.swarmdemo.app.api;

/**
 * Exception thrown when application user can not be found in the database
 */
public class NoSuchApplicationUserException extends Exception {

    /**
     * Initialise exception with error message
     * 
     * @param message
     *            Error message
     */
    public NoSuchApplicationUserException(final String message) {
        super(message);
    }

    /**
     * Initialise exception with root cause stack trace
     * 
     * @param th
     *            Root cause throwable
     */
    public NoSuchApplicationUserException(final Throwable th) {
        super(th);
    }
}
