package org.dekstroza.swarm.payments;

import java.util.Collection;
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

    /**
     * Return all records from payments table.
     * 
     * @return All payments in the database
     */
    Collection<Payment> getAllPayments();

    /**
     * Find record in payments table with given uuid
     * 
     * @param uuid
     *            Id of the record
     * @return Record from payments table with coresponding Id
     */
    Payment findById(final String uuid);

    /**
     * Mark record in payment table payed, setting passport attribute
     * 
     * @param uuid
     *            Id of the record being marked as payed off
     * @param passport
     *            Passport value to set on the record
     */
    void payday(final String uuid, final String passport);
}
