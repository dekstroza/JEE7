package org.dekstroza.swarm.payments.dao;

import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dekstroza.swarm.payments.api.Payments;
import org.slf4j.Logger;

@Stateless
@Local(PaymentsService.class)
public class PaymentsServiceImpl implements PaymentsService {

    @Inject
    private Logger logger;

    @PersistenceContext(name = "PaymentsPU")
    private EntityManager em;

    public UUID insertNewPayment(Payments payments) {
        final UUID uuid = UUID.randomUUID();
        payments.setId(uuid.toString());
        em.persist(payments);
        return uuid;
    }

}
