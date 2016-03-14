package org.dekstroza.swarm.payments;

import java.util.Collection;
import java.util.Date;
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

    @Override
    public UUID insertNewPayment(final Payment payment) {
        final UUID uuid = UUID.randomUUID();
        payment.setId(uuid.toString());
        em.persist(payment);
        return uuid;
    }

    @Override
    public Collection<Payment> getAllPayments() {
        return em.createQuery("SELECT p FROM Payment p", Payment.class).getResultList();
    }

    @Override
    public Payment findById(final String uuid) {
        return em.find(Payment.class, uuid);
    }

    @Override
    public void payday(final String uuid, final String passport) {
        final Payment payment = findById(uuid);
        if (payment != null) {
            payment.setCompleted(new Date());
            payment.setPassportId(passport);
            payment.setPayed(true);
            em.merge(payment);
        } else {
            throw new RuntimeException(new StringBuilder("Record with uuid=[").append(uuid).append("] was not found.").toString());
        }
    }
}
