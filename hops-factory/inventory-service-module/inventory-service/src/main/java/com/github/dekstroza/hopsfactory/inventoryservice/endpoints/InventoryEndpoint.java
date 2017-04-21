package com.github.dekstroza.hopsfactory.inventoryservice.endpoints;

import static com.github.dekstroza.hopsfactory.inventoryservice.InventoryServiceApplication.APPLICATION_INVENTORY_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.util.UUID;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.commons.rest.ExposeLogControl;
import com.github.dekstroza.hopsfactory.inventoryservice.domain.Inventory;
import com.github.dekstroza.hopsfactory.inventoryservice.domain.PersistanceHelper;

@RequestScoped
@Path("inventory")
public class InventoryEndpoint implements ExposeLogControl {

    public static final Logger log = LoggerFactory.getLogger(InventoryEndpoint.class);

    @EJB
    PersistanceHelper persistanceHelper;

    @Produces({ APPLICATION_INVENTORY_SERVICE_V1_JSON, APPLICATION_JSON })
    @PUT
    public void createNewInventory(@QueryParam("supplierId") UUID supplierId, @QueryParam("name") String name,
                                   @QueryParam("description") String description, @QueryParam("price") double price,
                                   @QueryParam("quantity") double quantity, @Suspended AsyncResponse response) {

        try {
            response.resume(status(CREATED).entity(persistanceHelper.persistInventory(new Inventory(supplierId, price, quantity, name, description)))
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
