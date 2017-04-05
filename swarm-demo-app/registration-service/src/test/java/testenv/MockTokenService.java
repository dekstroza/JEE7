package testenv;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1.0.0")
@Stateless
public class MockTokenService {

    final static String USERNAME = "username";
    final static String PASSWORD = "password";

    @Path("token")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Asynchronous
    public void generateToken(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                              @Suspended final AsyncResponse response) {
        response.resume(Response.status(Response.Status.OK).entity("123456").build());
    }
}
