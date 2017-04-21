package com.github.dekstroza.hopsfactory.customerservice.endpoints;

import static com.github.dekstroza.hopsfactory.customerservice.util.SecureRandomGenerator.secureRandom;
import static com.github.dekstroza.hopsfactory.customerservice.util.ValidationMessages.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.math.BigInteger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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

import com.github.dekstroza.hopsfactory.commons.rest.ExposeLogControl;
import com.github.dekstroza.hopsfactory.customerservice.CustomerServiceApplication;
import com.github.dekstroza.hopsfactory.customerservice.domain.Customer;
import com.github.dekstroza.hopsfactory.customerservice.domain.PersistanceHelper;

@RequestScoped
@Path("customer")
public class CustomerEndpoint implements ExposeLogControl {

    private static final Logger log = LoggerFactory.getLogger(CustomerEndpoint.class);

    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String EMAIL = "email";

    @EJB
    private PersistanceHelper persistanceHelper;

    @Produces({ CustomerServiceApplication.APPLICATION_CUSTOMER_SERVICE_V1_JSON, APPLICATION_JSON })
    @PUT
    public void createNewCustomer(@NotNull(message = MISSING_FIRSTNAME_MSG) @Size(min = 2, max = 100, message = FIRSTNAME_INVALID_LENGTH_MSG) @QueryParam(FIRSTNAME) String firstname,
                                  @NotNull(message = MISSING_LASTNAME_MSG) @Size(min = 2, max = 100, message = LASTNAME_INVALID_LENGTH_MSG) @QueryParam(LASTNAME) String lastname,
                                  @NotNull(message = MISSING_EMAIL_MSG) @Pattern(regexp = ".+@.+\\..+", message = INVALID_EMAIL_FORMAT_MSG) @QueryParam(EMAIL) String email,
                                  @Suspended AsyncResponse response) {
        try {
            Customer customer = new Customer(firstname, lastname, email, new BigInteger(130, secureRandom).toString(32));
            customer = persistanceHelper.persistCustomer(customer);
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
