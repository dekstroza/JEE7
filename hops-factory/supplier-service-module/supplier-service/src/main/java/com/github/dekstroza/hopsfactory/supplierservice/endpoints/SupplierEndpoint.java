package com.github.dekstroza.hopsfactory.supplierservice.endpoints;

import static com.github.dekstroza.hopsfactory.supplierservice.SupplierServiceApplication.APPLICATION_SUPPLIER_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;

import java.util.Optional;
import java.util.UUID;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.github.dekstroza.hopsfactory.supplierservice.domain.PersistanceHelper;
import com.github.dekstroza.hopsfactory.supplierservice.domain.Supplier;
import com.github.dekstroza.hopsfactory.supplierservice.util.ExposeLogControl;

import io.swagger.annotations.*;

@Api(value = "/supplier", description = "Operations on suppliers")
@Path("supplier")
@RequestScoped
public class SupplierEndpoint implements ExposeLogControl {

    @EJB
    private PersistanceHelper persistanceHelper;

    @ApiOperation(httpMethod = "POST", value = "Create new supplier", response = Supplier.class, produces = APPLICATION_JSON + ", "
            + APPLICATION_SUPPLIER_SERVICE_V1_JSON, consumes = APPLICATION_JSON + ", " + APPLICATION_SUPPLIER_SERVICE_V1_JSON)
    @ApiResponses(value = { @ApiResponse(code = 200, response = Supplier.class, message = ""),
            @ApiResponse(code = 400, message = "Invalid request") })
    @Consumes({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @Produces({ APPLICATION_JSON, APPLICATION_SUPPLIER_SERVICE_V1_JSON })
    @POST
    public void insertNewSupplier(@ApiParam(value = "Supplier to be created", required = true, allowableValues = "JSON representation of Supplier.class instance.") Supplier supplier,
                                  @Suspended AsyncResponse response) {
        try {
            response.resume(status(CREATED).entity(persistanceHelper.persistSupplier(supplier)).build());
        } catch (Exception e) {
            response.resume(status(BAD_REQUEST).entity(originalCause(e).getMessage()).build());
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
    public void getSupplierById(@ApiParam(value = "Id of the requested supplier", required = true, name = "id", allowableValues = "Valid UUID strings", example = "571fbefb-f96e-40c7-b699-94ac2403eab4") @PathParam("id") UUID id,
                                @Suspended AsyncResponse response) {
        try {
            Optional.of(persistanceHelper.findById(id)).ifPresent(x -> {
                response.resume(status(OK).entity(x).build());
                return;
            });
            response.resume(status(NOT_FOUND).build());
        } catch (Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).entity(originalCause(e)).build());
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
        try {
            response.resume(status(OK).entity(persistanceHelper.getAllSuppliers()).build());
        } catch (Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).entity(originalCause(e)).build());
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
