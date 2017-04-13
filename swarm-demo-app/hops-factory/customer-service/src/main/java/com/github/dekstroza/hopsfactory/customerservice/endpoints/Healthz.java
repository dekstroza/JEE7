package com.github.dekstroza.hopsfactory.customerservice.endpoints;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public class Healthz {

    @Path("healthz")
    @GET
    @Produces(TEXT_PLAIN)
    public Response basicHealthcheck() {
        return status(OK).build();
    }
}
