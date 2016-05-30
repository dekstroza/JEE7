package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1.0.0")
public class SecuredRestEndpoint {

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("secure/data")
    public Response showSomeSecureData() {

        return Response.status(Response.Status.OK).entity("Some super secret data").build();
    }
}
