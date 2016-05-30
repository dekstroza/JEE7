package io.dekstroza.github.jee7.swarmdemo.app.services;

import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.InvalidCredentialsException;

public interface AuthenticationService {

    /**
     * Authenticate user and return JWT token or throw exception if credentials are invalid
     * 
     * @param credentials
     *            Credentials to verify
     * @return JWT for this user
     * @throws InvalidCredentialsException
     *             if credentials provided are invalid
     */
    String authenticateUser(final Credentials credentials) throws InvalidCredentialsException;
}
