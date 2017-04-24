package com.github.dekstroza.hopsfactory.supplierservice.endpoints;

import com.github.dekstroza.hopsfactory.commons.rest.ExposeLogControl;
import com.github.dekstroza.hopsfactory.supplierservice.domain.Supplier;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.Optional;
import java.util.UUID;

import static com.github.dekstroza.hopsfactory.supplierservice.SupplierServiceApplication.APPLICATION_SUPPLIER_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;

@Transactional
@Api(value = "/supplier", description = "Operations on suppliers")
@Path("supplier")
@RequestScoped
@ExposeLogControl
public class SupplierEndpoint {

    private static final Logger log = LoggerFactory.getLogger(SupplierEndpoint.class);

    @PersistenceContext(unitName = "SupplierPU")
    private EntityManager entityManager;

    @Resource(lookup = "jboss/datasources/SupplierDS")
    private DataSource dataSource;

    @ApiOperation(httpMethod = "POST", value = "Create new supplier", response = Supplier.class, produces = APPLICATION_JSON + ", "
               + APPLICATION_SUPPLIER_SERVICE_V1_JSON, consumes = APPLICATION_JSON + ", " + APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 200, response = Supplier.class, message = ""),
               @ApiResponse(code = 400, message = "Invalid request") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Produces({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @POST
    public void insertNewSupplier(
               @ApiParam(value = "Supplier to be created", required = true, allowableValues = "JSON representation of Supplier.class instance.")
                          Supplier supplier, @Suspended AsyncResponse response) {
        log.debug("Creating new supplier={}", supplier);
        try {
            entityManager.persist(supplier);
            response.resume(status(CREATED).entity(supplier).build());
        } catch (Exception e) {
            printDebugInfoOnError(e);
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    @ApiOperation(httpMethod = "PUT", value = "Modify supplier", response = Supplier.class, produces = APPLICATION_JSON, consumes = APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 200, response = Supplier.class, message = ""), @ApiResponse(code = 404, message = "No such supplier"),
               @ApiResponse(code = 500, message = "Error contacting database") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Produces(APPLICATION_JSON)
    @PUT
    public void updateSupplier(
               @ApiParam(value = "Updated supplier", required = true, allowableValues = "JSON representation of Supplier.class instance.")
                          Supplier supplier, @Suspended AsyncResponse response) {
        log.debug("Update supplier={}", supplier);
        try {
            Optional.of(entityManager.find(Supplier.class, supplier.getId())).ifPresent(x -> response.resume(status(OK)
                       .entity(entityManager.merge(x.copyFrom(supplier))).build()));
            response.resume(status(NOT_FOUND).entity("No such supplier").build());
        } catch (Exception e) {
            printDebugInfoOnError(e);
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    @ApiOperation(httpMethod = "PUT", value = "Modify supplier with given id", response = Supplier.class, produces = APPLICATION_JSON, consumes = APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 200, response = Supplier.class, message = ""), @ApiResponse(code = 404, message = "No such supplier"),
               @ApiResponse(code = 500, message = "Error contacting database") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Produces(APPLICATION_JSON)
    @PUT
    @Path("{id}")
    public void updateSupplierById(
               @ApiParam(value = "Id of supplier to be updated", required = true, allowableValues = "Valid UUID.") @PathParam("id") UUID id,
               @ApiParam(value = "Modified supplier", required = true, allowableValues = "JSON representation of Supplier.class instance.")
                          Supplier supplier, @Suspended AsyncResponse response) {
        log.debug("Update supplier with id={}", id);
        try {
            Optional.of(entityManager.find(Supplier.class, id)).ifPresent(x -> response.resume(status(OK)
                       .entity(entityManager.merge(x.copyFrom(supplier))).build()));
            response.resume(status(NOT_FOUND).entity("No such supplier").build());
        } catch (Exception e) {
            printDebugInfoOnError(e);
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    @ApiOperation(httpMethod = "DELETE", value = "Delete supplier", response = Supplier.class, produces = APPLICATION_JSON, consumes = APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 204, message = ""), @ApiResponse(code = 404, message = "No such supplier"),
               @ApiResponse(code = 500, message = "Error contacting database") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Produces(APPLICATION_JSON)
    @DELETE
    public void deleteSupplier(
               @ApiParam(value = "Supplier to be deleted.", required = true, allowableValues = "JSON representation of Supplier.class instance.")
                          Supplier supplier, @Suspended AsyncResponse response) {
        log.debug("Delete supplier={}", supplier);
        try {
            Optional.of(entityManager.find(Supplier.class, supplier.getId())).ifPresent(x -> {
                entityManager.remove(x);
                response.resume(status(NO_CONTENT).build());
            });
            response.resume(status(NOT_FOUND).entity("No such supplier").build());
        } catch (Exception e) {
            printDebugInfoOnError(e);
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    @ApiOperation(httpMethod = "DELETE", value = "Delete supplier", produces = APPLICATION_JSON, consumes = APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 204, message = ""), @ApiResponse(code = 404, message = "No such supplier"),
               @ApiResponse(code = 500, message = "Error contacting database") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON, TEXT_PLAIN })
    @Produces(APPLICATION_JSON)
    @DELETE
    @Path("{id}")
    public void deleteSupplier(
               @ApiParam(value = "Id of supplier to be removed.", required = true, allowableValues = "Valid UUID.") @PathParam("id") UUID id,
               @Suspended AsyncResponse response) {
        log.debug("Delete supplier with ID={}", id);
        try {
            Optional.of(entityManager.find(Supplier.class, id)).ifPresent(x -> {
                entityManager.remove(x);
                response.resume(status(NO_CONTENT).build());
            });
            response.resume(status(NOT_FOUND).entity("No such supplier").build());
        } catch (Exception e) {
            printDebugInfoOnError(e);
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    @ApiOperation(httpMethod = "GET", value = "Find supplier with given id.", response = Supplier.class, produces = APPLICATION_JSON + ", "
               + APPLICATION_SUPPLIER_SERVICE_V1_JSON, consumes = APPLICATION_JSON + ", " + APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 200, response = Supplier.class, message = ""),
               @ApiResponse(code = 404, message = "Request with given id does not exist."),
               @ApiResponse(code = 500, message = "Internal server error.") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Produces({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Path("{id}")
    @GET
    public void getSupplierById(
               @ApiParam(value = "Id of the requested supplier", required = true, name = "id", allowableValues = "Valid UUID strings", example = "571fbefb-f96e-40c7-b699-94ac2403eab4")
               @PathParam("id") UUID id, @Suspended AsyncResponse response) {
        log.debug("getSupplierById called with id={}", id);
        try {
            Optional.of(entityManager.find(Supplier.class, id)).ifPresent(x -> response.resume(status(OK).entity(x).build()));
            response.resume(status(NOT_FOUND).build());
        } catch (Exception e) {
            printDebugInfoOnError(e);
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    @ApiOperation(httpMethod = "GET", value = "Find all suppliers", response = Supplier.class, responseContainer = "List", produces = APPLICATION_JSON
               + ", " + APPLICATION_SUPPLIER_SERVICE_V1_JSON, consumes = APPLICATION_JSON + ", " + APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 200, response = Supplier.class, message = "", responseContainer = "List"),
               @ApiResponse(code = 500, message = "Internal server error.") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Produces({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @GET
    public void getAllSuppliers(@Suspended AsyncResponse response) {
        log.debug("getAllSuppliers called");
        try {
            response.resume(status(OK).entity(entityManager.createQuery("SELECT s FROM supplier s", Supplier.class).getResultList()).build());
        } catch (Exception e) {
            printDebugInfoOnError(e);
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    private void printDebugInfoOnError(Throwable t) {
        if (log.isDebugEnabled()) {
            t.printStackTrace();
        }
    }

    @javax.enterprise.inject.Produces
    public DataSource getDataSource() {
        return dataSource;
    }

}
