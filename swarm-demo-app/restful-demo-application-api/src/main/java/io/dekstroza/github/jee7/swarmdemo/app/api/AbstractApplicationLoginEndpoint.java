package io.dekstroza.github.jee7.swarmdemo.app.api;

import static io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationConstants.PASSWORD;
import static io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationConstants.USERNAME;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public abstract class AbstractApplicationLoginEndpoint {

    protected abstract EntityManager getEntityManager();

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

    protected ApplicationUser findApplicationUserByCredentials(final Credentials credentials) {
        return getEntityManager()
                .createQuery("SELECT au FROM ApplicationUser au WHERE au.username = :username AND au.password = :password", ApplicationUser.class)
                .setParameter(USERNAME, credentials.getUsername()).setParameter(PASSWORD, credentials.getPassword()).getSingleResult();
    }

}
