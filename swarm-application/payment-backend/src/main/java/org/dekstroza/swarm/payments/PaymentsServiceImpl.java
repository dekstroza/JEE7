package org.dekstroza.swarm.payments;

import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dekstroza.swarm.payments.entities.Payment;
import org.slf4j.Logger;

/**
 * Implementation of payment service
 */
@Stateless
@Local(PaymentsService.class)
public class PaymentsServiceImpl implements PaymentsService {

    @Inject
    private Logger logger;

    @PersistenceContext(name = "PaymentsPU")
    private EntityManager em;

    /**
     * @see PaymentsService
     */
    public UUID insertNewPayment(Payment payment) {
        final UUID uuid = UUID.randomUUID();
        payment.setId(uuid.toString());
        em.persist(payment);
        return uuid;
    }

}
