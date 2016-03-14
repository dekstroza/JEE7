package org.dekstroza.swarm.payments.endpoint;

import static javax.ws.rs.core.Response.Status.OK;
import static org.dekstroza.swarm.payments.PaymentResponse.ResponseStatus.ERROR;
import static org.dekstroza.swarm.payments.PaymentResponse.ResponseStatus.SUCCESS;

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

import org.dekstroza.swarm.payments.PaymentResponse;
import org.dekstroza.swarm.payments.PaymentsService;
import org.dekstroza.swarm.payments.entities.Payment;
import org.slf4j.Logger;

/**
 * Payment rest endpoint, handling all payments rest calls
 */

@Path("v1.0")
public class PaymentsRestEndpoint {

    @Inject
    private Logger logger;

    @EJB
    private PaymentsService paymentsService;

    @POST
    @Path("payment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertNewPayment(@NotNull final Payment payment) {
        logger.debug("Inserting new payment record {}", payment);
        try {
            final UUID uuid = paymentsService.insertNewPayment(payment);
            PaymentResponse paymentResponse = new PaymentResponse(uuid.toString(), SUCCESS);
            return Response.status(OK).entity(paymentResponse).build();
        } catch (final Exception exception) {
            PaymentResponse paymentResponse = new PaymentResponse(exception.getMessage(), ERROR);
            return Response.status(OK).entity(paymentResponse).build();
        }

    }

}
