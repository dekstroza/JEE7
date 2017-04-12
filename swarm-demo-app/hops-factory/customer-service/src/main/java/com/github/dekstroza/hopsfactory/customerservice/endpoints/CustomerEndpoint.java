package com.github.dekstroza.hopsfactory.customerservice.endpoints;

import static com.github.dekstroza.hopsfactory.customerservice.endpoints.SecureRandomGenerator.secureRandom;

import java.math.BigInteger;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.github.dekstroza.hopsfactory.customerservice.domain.Customer;

@Path("v1.0.0")
@Transactional
@RequestScoped
public class CustomerEndpoint {

    @PersistenceContext(unitName = "CustomerPU")
    private EntityManager entityManager;

    @PUT
    @Path("customer")
    public void createNewCustomer(@QueryParam("firstname") String firstname, @QueryParam("lastname") String lastname,
                                  @QueryParam("email") String email, @Suspended AsyncResponse response) {
        Customer customer = new Customer();
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setEmail(email);
        customer.setPassword(new BigInteger(130, secureRandom).toString(32));
        entityManager.persist(customer);
    }
}
