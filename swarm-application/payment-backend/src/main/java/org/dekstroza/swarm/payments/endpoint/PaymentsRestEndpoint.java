package org.dekstroza.swarm.payments.endpoint;

import java.util.UUID;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dekstroza.swarm.payments.api.Payment;
import org.dekstroza.swarm.payments.api.PaymentInsertResponse;
import org.dekstroza.swarm.payments.dao.PaymentsService;
import org.slf4j.Logger;

/**
 * Payments rest endpoint, handling all payments rest calls
 */

@Path("v1.0")
public class PaymentsRestEndpoint {

    @Inject
    private Logger logger;

    @EJB
    private PaymentsService paymentsService;

    @POST
    @Path("payments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertNewPayment(@NotNull final Payment payment) {
        logger.debug("Inserting new payment record {}", payment);
        try {
            final UUID uuid = paymentsService.insertNewPayment(payment);
            PaymentInsertResponse paymentInsertResponse = new PaymentInsertResponse("OK", uuid.toString());
            return Response.status(Response.Status.OK).entity(paymentInsertResponse).build();
        } catch (final Exception exception) {
            PaymentInsertResponse paymentInsertResponse = new PaymentInsertResponse("ERROR", exception.getMessage());
            return Response.status(Response.Status.OK).entity(paymentInsertResponse).build();
        }

    }

}
