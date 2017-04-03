package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.domain.TokenService.PASSWORD;
import static io.dekstroza.github.jee7.swarmdemo.app.domain.TokenService.USERNAME;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ejb.Asynchronous;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.dekstroza.github.jee7.swarmdemo.app.domain.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.domain.TokenService;

@RequestScoped
@Path("v1.0.0")
public class TokenGeneratorEndpoint {

    @Path("token")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Asynchronous
    public void generateToken(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                              @Suspended final AsyncResponse response) {
        final String jwtToken = new Credentials(username, password).generateJWToken();
        response.resume(status(OK).entity(jwtToken).build());
    }

}
