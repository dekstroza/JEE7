package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestScoped
@Path("v1.0.0")
@Api(value = "/healthz", description = "Heathcheck endpoint for token service", tags = "healthcheck")
public class HealthzEndpoint {

    @PermitAll
    @Produces(APPLICATION_JSON)
    @GET
    @Path("healthz")
    @ApiOperation(value = "Helthcheck endpoint for token service", notes = "Returns HTTP 200 if application is healthy", response = String.class)
    public Response healthCheck() {
        return status(OK).entity("OK").build();
    }
}
