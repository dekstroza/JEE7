package com.github.dekstroza.hopsfactory.customerservice.endpoints;

import static com.github.dekstroza.hopsfactory.customerservice.CustomerServiceApplication.APPLICATION_CUSTOMER_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.commons.rest.ExposeLogControl;
import com.github.dekstroza.hopsfactory.customerservice.domain.Customer;

@Transactional
@RequestScoped
@Path("customer")
public class CustomerEndpoint implements ExposeLogControl {

    private static final Logger log = LoggerFactory.getLogger(CustomerEndpoint.class);

    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String EMAIL = "email";

    @PersistenceContext(unitName = "CustomerPU")
    private EntityManager entityManager;

    @Consumes(APPLICATION_JSON)
    @Produces({ APPLICATION_CUSTOMER_SERVICE_V1_JSON, APPLICATION_JSON })
    @POST
    public void createNewCustomer(Customer customer, @Suspended AsyncResponse response) {
        try {
            entityManager.persist(customer);
            response.resume(status(CREATED).entity(customer).build());
        } catch (Exception e) {
            response.resume(status(BAD_REQUEST).entity(originalCause(e).getMessage()).build());
        }
    }

    private Throwable originalCause(Exception e) {
        Throwable t = e.getCause();
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

}
