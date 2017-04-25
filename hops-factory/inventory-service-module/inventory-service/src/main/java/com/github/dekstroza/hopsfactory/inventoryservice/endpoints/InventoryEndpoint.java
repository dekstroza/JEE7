package com.github.dekstroza.hopsfactory.inventoryservice.endpoints;

import com.github.dekstroza.hopsfactory.commons.rest.ExposeLogControl;
import com.github.dekstroza.hopsfactory.inventoryservice.domain.Inventory;

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

import static com.github.dekstroza.hopsfactory.inventoryservice.InventoryServiceApplication.APPLICATION_INVENTORY_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.status;

@Transactional
@RequestScoped
@Path("inventory")
@ExposeLogControl
public class InventoryEndpoint {

    @PersistenceContext(unitName = "InventoryPU")
    private EntityManager entityManager;

    @Resource(lookup = "jboss/datasources/InventoryDS")
    private DataSource dataSource;

    @Consumes(APPLICATION_JSON)
    @Produces({ APPLICATION_INVENTORY_SERVICE_V1_JSON, APPLICATION_JSON })
    @POST
    public void createNewInventory(Inventory inventory, @Suspended AsyncResponse response) {
        try {
            entityManager.persist(inventory);
            response.resume(status(CREATED).entity(inventory).build());
        } catch (Exception e) {
            response.resume(status(BAD_REQUEST).entity(e).build());
        }

    }

    @javax.enterprise.inject.Produces
    public DataSource getDataSource() {
        return dataSource;
    }

}
