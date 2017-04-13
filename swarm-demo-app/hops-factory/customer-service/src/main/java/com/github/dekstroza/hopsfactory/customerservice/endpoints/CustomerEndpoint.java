package com.github.dekstroza.hopsfactory.customerservice.endpoints;

import static com.github.dekstroza.hopsfactory.customerservice.endpoints.SecureRandomGenerator.secureRandom;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.math.BigInteger;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.customerservice.domain.Customer;

@Path("v1.0.0")
@RequestScoped
public class CustomerEndpoint {

    private static final Logger log = LoggerFactory.getLogger(CustomerEndpoint.class);

    @PersistenceContext(unitName = "CustomerPU")
    private EntityManager entityManager;

    @Transactional
    @Produces(APPLICATION_JSON)
    @PUT
    @Path("customer")
    public void createNewCustomer(@NotNull(message = "Parameter firstname can not be null.") @Size(min = 2, max = 100, message = "Parameter firstname must be between 2 and 100 characters long.") @QueryParam("firstname") String firstname,
                                  @NotNull(message = "Parameter lastname can not be null.") @Size(min = 2, max = 100, message = "Parameter lastname must be between 2 and 100 characters long.") @QueryParam("lastname") String lastname,
                                  @NotNull(message = "Parameter email can not be null.") @Size(min = 5, max = 100, message = "Parameter email must be between 2 and 100 characters long.") @Pattern(regexp = ".+@.+\\..+", message = "Parameter email must be valid mail address.") @QueryParam("email") String email,
                                  @Suspended AsyncResponse response) {
        try {
            final Customer customer = new Customer(firstname, lastname, email, new BigInteger(130, secureRandom).toString(32));
            entityManager.persist(customer);
            response.resume(status(CREATED).entity(customer).build());
        } catch (Exception e) {
            log.error("Error creating customer.", e);
            response.resume(e);
        }
    }
}
