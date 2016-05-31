package io.dekstroza.github.jee7.swarmdemo.app.services;

import java.util.Collection;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.NoSuchApplicationUserException;

/**
 * Application User Service
 */
public interface ApplicationUserService {

    /**
     * Find application user by id
     * 
     * @param id
     *            Id of this application user
     * @return ApplicationUser entity or throws NoResultException
     * @throws NoSuchApplicationUserException
     *             when application user can not be found.
     */
    ApplicationUser findApplicationUserById(final int id) throws NoSuchApplicationUserException;

    /**
     * Find application user by credentials, username & password
     * 
     * @param credentials
     *            Credentials
     * @return ApplicationUser entity or throws NoResultException
     *
     * @throws javax.persistence.NoResultException
     *             if no such user
     */
    ApplicationUser findApplicationUserByCredentials(final Credentials credentials);

    /**
     * Find all application users
     * 
     * @return Will return collection of all application users
     */
    Collection<ApplicationUser> findAllApplicationUsers();

    /**
     * Insert new application user
     * 
     * @param applicationUser
     *            ApplicationUser to be persisted
     * @return Persisted ApplicatonUser entity containing valid id
     */
    ApplicationUser insertApplicationUser(final ApplicationUser applicationUser);
}
