package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.*;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.InvalidCredentialsException;
import io.dekstroza.github.jee7.swarmdemo.app.services.AuthenticationService;

@Stateless
@Path("v1.0.0")
public class ApplicationLoginEndpoint {

    @Inject
    private AuthenticationService authenticationService;

    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Asynchronous
    public void applicationLogin(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                                 final @Suspended AsyncResponse response) {
        try {
            final String JWToken = authenticationService.authenticateUser(new Credentials(username, password));
            response.resume(status(OK).header(AUTHORIZATION, JWToken).build());
        } catch (final InvalidCredentialsException ie) {
            response.resume(status(BAD_REQUEST).entity(ie.getMessage()).build());
        } catch (final Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).build());
        }

    }

}
