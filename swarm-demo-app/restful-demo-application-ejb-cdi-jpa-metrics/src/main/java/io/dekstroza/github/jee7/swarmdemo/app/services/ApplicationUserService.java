package io.dekstroza.github.jee7.swarmdemo.app.services;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.PASSWORD;
import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.USERNAME;

import java.util.Collection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.NoSuchApplicationUserException;

@Stateless
@LocalBean
public class ApplicationUserService {

    @PersistenceContext
    private EntityManager em;

    public ApplicationUser findApplicationUserById(int id) throws NoSuchApplicationUserException {
        try {
            return em.find(ApplicationUser.class, id);
        } catch (final NoResultException nre) {
            throw new NoSuchApplicationUserException(new StringBuilder("Application user with id=").append(id).append(" does not exist.").toString());
        }

    }

    public ApplicationUser findApplicationUserByCredentials(final Credentials credentials) {
        return em.createQuery("SELECT au FROM ApplicationUser au WHERE au.username = :username AND au.password = :password", ApplicationUser.class)
                .setParameter(USERNAME, credentials.getUsername()).setParameter(PASSWORD, credentials.getPassword()).getSingleResult();
    }

    public Collection<ApplicationUser> findAllApplicationUsers() {
        Collection<ApplicationUser> applicationUsers = em.createQuery("SELECT au FROM ApplicationUser au", ApplicationUser.class).getResultList();
        return applicationUsers;
    }

    public ApplicationUser insertApplicationUser(final ApplicationUser applicationUser) {
        em.persist(applicationUser);
        return applicationUser;
    }
}
