package com.github.dekstroza.hopsfactory.orderservice.endpoints;

import static com.github.dekstroza.hopsfactory.orderservice.OrderServiceApplication.APPLICATION_ORDER_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.commons.rest.ExposeLogControl;
import com.github.dekstroza.hopsfactory.orderservice.domain.Order;

@Transactional
@Path("order")
@RequestScoped
@ExposeLogControl
public class OrderEndpoint {

    public static final Logger log = LoggerFactory.getLogger(OrderEndpoint.class);

    @Resource(lookup = "jboss/datasources/OrderDS")
    private DataSource dataSource;

    @PersistenceContext(unitName = "OrderPU")
    private EntityManager entityManager;

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces({ APPLICATION_JSON, APPLICATION_ORDER_SERVICE_V1_JSON })
    public void insertNewOrder(Order order, @Suspended AsyncResponse response) {
        try {
            entityManager.persist(order);
            response.resume(Response.status(CREATED).entity(order).build());
        } catch (Exception e) {
            response.resume(status(BAD_REQUEST).entity(originalCause(e).getMessage()).build());

        }
    }

    @javax.enterprise.inject.Produces
    public DataSource getDataSource() {
        return dataSource;
    }

    private Throwable originalCause(Exception e) {
        Throwable t = e.getCause();
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }
}
