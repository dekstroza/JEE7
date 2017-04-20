package com.github.dekstroza.hopsfactory.orderservice.endpoints;

import static com.github.dekstroza.hopsfactory.orderservice.OrderServiceApplication.APPLICATION_ORDER_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.util.Date;
import java.util.UUID;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.orderservice.domain.Order;
import com.github.dekstroza.hopsfactory.orderservice.domain.PersistanceHelper;
import com.github.dekstroza.hopsfactory.orderservice.util.ExposeLogControl;

@Path("order")
@RequestScoped
public class OrderEndpoint implements ExposeLogControl {

    public static final Logger log = LoggerFactory.getLogger(OrderEndpoint.class);

    @EJB
    private PersistanceHelper persistanceHelper;

    @POST
    @Produces({ APPLICATION_JSON, APPLICATION_ORDER_SERVICE_V1_JSON })
    public void insertNewOrder(@QueryParam("inventory_id") UUID inventoryId, @QueryParam("customerId") UUID customerId,
                               @QueryParam("quantity") double quantity, @QueryParam("price") double price, @Suspended AsyncResponse response) {
        try {
            response.resume(Response.status(CREATED)
                    .entity(persistanceHelper.persistInventory(new Order(inventoryId, customerId, quantity, price, "NEW ORDER", new Date())))
                    .build());
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
