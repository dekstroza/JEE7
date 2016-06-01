package io.dekstroza.github.jee7.swarmdemo.app.services.impl;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.NoSuchApplicationUserException;
import io.dekstroza.github.jee7.swarmdemo.app.services.ApplicationUserService;

@Stateless
@Local(ApplicationUserService.class)
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationUserService.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public ApplicationUser findApplicationUserById(int id) throws NoSuchApplicationUserException {
        try {
            return em.find(ApplicationUser.class, id);
        } catch (final NoResultException nre) {
            throw new NoSuchApplicationUserException(new StringBuilder("Application user with id=").append(id).append(" does not exist.").toString());
        }

    }

    @Override
    public ApplicationUser findApplicationUserByCredentials(Credentials credentials) {
        return em.createQuery("SELECT au FROM ApplicationUser au WHERE au.username = :username AND au.password = :password", ApplicationUser.class)
                .setParameter("username", credentials.getUsername()).setParameter("password", credentials.getPassword()).getSingleResult();
    }

    @Override
    public Collection<ApplicationUser> findAllApplicationUsers() {
        Collection<ApplicationUser> applicationUsers = em.createQuery("SELECT au FROM ApplicationUser au", ApplicationUser.class).getResultList();
        return applicationUsers;
    }

    @Override
    public ApplicationUser insertApplicationUser(final ApplicationUser applicationUser) {
        em.persist(applicationUser);
        return applicationUser;
    }
}
