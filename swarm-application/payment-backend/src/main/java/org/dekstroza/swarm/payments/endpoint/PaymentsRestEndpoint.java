package org.dekstroza.swarm.payments.endpoint;

import static javax.ws.rs.core.Response.Status.OK;
import static org.dekstroza.swarm.payments.PaymentResponse.ResponseStatus.ERROR;
import static org.dekstroza.swarm.payments.PaymentResponse.ResponseStatus.SUCCESS;

import java.util.Collection;
import java.util.UUID;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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

    /**
     * Insert new payment record into payments table
     * 
     * @param payment
     *            Payment record to be inserted
     * @return PaymentResponse with status SUCCESS and message containing record UUID if inserted, or status ERROR and message containing error
     *         description
     */
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

    /**
     * Return all records from payments table
     * 
     * @return Collection of all records in payments table
     */
    @GET
    @Path("payment")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Payment> getAllPayments() {
        return paymentsService.getAllPayments();
    }

    /**
     * Find record in payments table with given id
     * 
     * @param uuid
     *            Id of the record
     * @return Record matching given id
     */
    @GET
    @Path("payment/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment findPayment(@PathParam("uuid") final String uuid) {
        return paymentsService.findById(uuid);
    }

    @PUT
    @Path("payment/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response payday(@PathParam("uuid") String uuid, @FormParam("passport") String passport) {
        try {
            paymentsService.payday(uuid, passport);
            PaymentResponse paymentResponse = new PaymentResponse(uuid.toString(), SUCCESS);
            return Response.status(OK).entity(paymentResponse).build();

        } catch (Exception exception) {
            PaymentResponse paymentResponse = new PaymentResponse(exception.getMessage(), ERROR);
            return Response.status(OK).entity(paymentResponse).build();
        }
    }

}
