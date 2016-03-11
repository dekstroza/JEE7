package org.dekstroza.swarm.payments.endpoint;

import java.sql.Connection;
import java.sql.SQLException;

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
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dekstroza.swarm.payments.api.Payment;
import org.slf4j.Logger;

/**
 * Payments rest endpoint, handling all payments rest calls
 */

@Path("payments")
public class PaymentsRestEndpoint extends Application {

    @Inject
    private Logger logger;

    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertNewPayment(@NotNull final Payment payment) throws NamingException, SQLException {
        logger.info("Inserting new payment record {}", payment);
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jboss/datasources/ExampleDS");
        Connection conn = ds.getConnection();
        logger.info("conn {}", conn);
        conn.close();
        return Response.status(Response.Status.OK).entity(payment).build();
    }
}
