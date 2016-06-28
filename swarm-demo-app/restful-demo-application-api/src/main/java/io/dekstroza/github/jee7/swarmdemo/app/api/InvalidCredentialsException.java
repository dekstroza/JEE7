package io.dekstroza.github.jee7.swarmdemo.app.api;

/**
 * Exception thrown in case credentials are invalid
 */
public class InvalidCredentialsException extends Exception {

    /**
     * Constructor for InvalidCredentialException taking message to be returned
     * 
     * @param message
     *            Message returned to the caller
     */
    public InvalidCredentialsException(final String message) {
        super(message);
    }
}
