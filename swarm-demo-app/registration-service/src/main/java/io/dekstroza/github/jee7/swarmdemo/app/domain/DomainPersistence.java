package io.dekstroza.github.jee7.swarmdemo.app.domain;

import io.dekstroza.github.jee7.swarmdemo.app.endpoints.RegistrationEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.UUID;

@Stateless
public class DomainPersistence {


    private static final Logger log = LoggerFactory.getLogger(DomainPersistence.class);

    @PersistenceContext(unitName = "RegistrationsPU")
    EntityManager entityManager;

    public RegistrationInfo persistRegistrationInfo(final RegistrationInfo info) {
        log.info("calling persist on {}", info);
        info.setId(UUID.randomUUID().toString());
        info.setCreationDate(new Date());
        try {
            entityManager.persist(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("persisted {}", info.getCreationDate());
        return info;
    }
}
