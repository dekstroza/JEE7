package org.dekstroza.swarm.payments.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.dekstroza.swarm.payments.api.Payment;
import org.slf4j.Logger;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Local(PaymentsService.class)
public class PaymentsServiceImpl implements PaymentsService {

    @Inject
    private Logger logger;

    @Resource(lookup = "java:jboss/datasources/BackendDS")
    private DataSource dataSource;

    private static final String INSERT_QUERY = "INSERT INTO public.payments(id,firstname,lastname,phone,total_ammount,deducted_fee_ammount,receiver_location_id,sender_location_id) VALUES (?,?,?,?,?,?,?,?)";

    public UUID insertNewPayment(final Payment payment) throws SQLException {
        Connection conn = dataSource.getConnection();
        return insertRecord(payment, conn);
    }

    UUID insertRecord(final Payment payment, final Connection connection) throws SQLException {
        connection.setAutoCommit(true);
        final UUID uuid = UUID.randomUUID();
        final PreparedStatement preparedStatement = prepareStatement(uuid, payment, connection);
        preparedStatement.execute();
        return uuid;
    }

    PreparedStatement prepareStatement(final UUID uuid, final Payment payment, final Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, payment.getFirstName());
        preparedStatement.setString(3, payment.getLastName());
        preparedStatement.setString(4, payment.getPhoneNumber());
        preparedStatement.setInt(5, payment.getTotalAmmount());
        preparedStatement.setInt(6, payment.getFeeDeductedAmmount());
        preparedStatement.setInt(7, payment.getReceiverLocationId());
        preparedStatement.setInt(8, payment.getSenderLocationId());
        return preparedStatement;
    }
}
