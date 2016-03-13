package org.dekstroza.swarm.payments.endpoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dekstroza.swarm.payments.api.Payment;
import org.dekstroza.swarm.payments.api.PaymentInsertResponse;
import org.slf4j.Logger;

/**
 * Payments rest endpoint, handling all payments rest calls
 */

@Path("payments")
public class PaymentsRestEndpoint {

    @Inject
    private Logger logger;

    private static final String INSERT_QUERY = "INSERT INTO public.payments(id,firstname,lastname,phone,total_ammount,deducted_fee_ammount,receiver_location_id,sender_location_id) VALUES (?,?,?,?,?,?,?,?)";

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertNewPayment(@NotNull final Payment payment) throws NamingException, SQLException {
        logger.debug("Inserting new payment record {}", payment);
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jboss/datasources/ExampleDS");
        Connection conn = ds.getConnection();
        try {
            final UUID uuid = insertRecord(payment, conn);
            final PaymentInsertResponse response = new PaymentInsertResponse(uuid.toString(), "SUCCESS");
            return Response.status(Response.Status.OK).entity(response).build();
        } catch (SQLException e) {
            e.printStackTrace();
            final PaymentInsertResponse response = new PaymentInsertResponse(e.getMessage(), "FAILURE");
            return Response.status(Response.Status.OK).entity(response).build();
        } finally {
            conn.close();
        }

    }

    private UUID insertRecord(final Payment payment, final Connection connection) throws SQLException {
        connection.setAutoCommit(true);
        final UUID uuid = UUID.randomUUID();
        final PreparedStatement preparedStatement = prepareStatement(uuid, payment, connection);
        preparedStatement.execute();
        return uuid;
    }

    private PreparedStatement prepareStatement(final UUID uuid, final Payment payment, final Connection connection) throws SQLException {
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
