package io.dekstroza.github.jee7.swarmdemo.app.endpoints.rest;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("v1.0.0")
public class HealthzEndpoint {

    @Path("healthz")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public void serviceHealthcheck(@Suspended AsyncResponse response) {
        response.resume(status(OK));
    }
}
