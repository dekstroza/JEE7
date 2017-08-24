package com.ericsson.techday;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;

@Path("admin")
@RequestScoped
public class MessageBrokerAdminEndpoint {

    @GET
    @Produces(TEXT_PLAIN)
    public Response getState() {
        return status(OK).entity("Message broker is alive.").build();
    }
}
