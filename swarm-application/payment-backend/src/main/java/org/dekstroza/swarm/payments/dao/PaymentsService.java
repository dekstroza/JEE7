package org.dekstroza.swarm.payments.dao;

import java.util.UUID;

import org.dekstroza.swarm.payments.api.Payments;

public interface PaymentsService {

    UUID insertNewPayment(final Payments payments);
}
