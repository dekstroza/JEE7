package org.dekstroza.swarm.payments.dao;

import java.sql.SQLException;
import java.util.UUID;

import org.dekstroza.swarm.payments.api.Payment;

public interface PaymentsService {

    UUID insertNewPayment(final Payment payment) throws SQLException;
}
