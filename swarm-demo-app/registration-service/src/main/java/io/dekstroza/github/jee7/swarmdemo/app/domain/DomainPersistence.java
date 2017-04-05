package io.dekstroza.github.jee7.swarmdemo.app.domain;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class DomainPersistence {

    private static final Logger log = LoggerFactory.getLogger(DomainPersistence.class);

    @PersistenceContext(unitName = "RegistrationsPU")
    EntityManager entityManager;

    public RegistrationInfo persistRegistrationInfo(final RegistrationInfo info) {
        log.info("calling persist on {}", info);
        try {
            info.setCreationDate(new Date());
            entityManager.persist(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("persisted {}", info);
        return info;
    }
}
