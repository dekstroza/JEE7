package org.dekstroza.swarm.application.security;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import com.dekstroza.swarm.security.entities.User;

@Stateless
@Local(SecurityService.class)
public class SecurityServiceImpl implements SecurityService {

    @Inject
    private Logger logger;

    @PersistenceContext(unitName = "SecurityPU")
    private EntityManager em;

    @Override
    public User findUserByUsername(final String username) {
        return em.createNamedQuery(User.FIND_BY_USERNAME_QUERY, User.class).setParameter("email", username).getSingleResult();
    }

    @Override
    public void insertUser(final User user) {
        em.persist(user);
    }
}
