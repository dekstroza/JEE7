package org.dekstroza.swarm.payments;

import java.util.UUID;

import org.dekstroza.swarm.payments.entities.Payment;

/**
 * Service responsible for payments
 */
public interface PaymentsService {

    /**
     * Insert new entry into payment table
     * 
     * @param payment
     *            Payment to be inserted
     * @return UUID which will be used as unique identifier for this payment record
     */
    UUID insertNewPayment(final Payment payment);
}
