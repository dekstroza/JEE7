package io.dekstroza.github.jee7.swarmdemo.app.api;

import javax.persistence.NoResultException;

public abstract class AbstractApplicationLoginEndpoint {

    protected String authenticateUser(Credentials credentials) throws InvalidCredentialsException {
        try {
            final ApplicationUser applicationUser = findApplicationUserByCredentials(credentials);
            return credentials.generateJWToken();
        } catch (final NoResultException nre) {
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (final Exception e) {
            throw new InvalidCredentialsException(e.getMessage());
        }

    }

    protected abstract ApplicationUser findApplicationUserByCredentials(Credentials credentials);

}
