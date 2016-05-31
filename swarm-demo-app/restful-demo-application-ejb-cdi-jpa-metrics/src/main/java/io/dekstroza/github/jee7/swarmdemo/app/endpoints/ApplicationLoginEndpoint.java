package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.Status.*;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.InvalidCredentialsException;
import io.dekstroza.github.jee7.swarmdemo.app.services.AuthenticationService;
import io.dekstroza.github.jee7.swarmdemo.app.services.impl.StatsdCommunicator;

@Path("v1.0.0")
public class ApplicationLoginEndpoint {

    @EJB
    private AuthenticationService authenticationService;

    @EJB
    private StatsdCommunicator statsdCommunicator;

    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response applicationLogin(@QueryParam("username") final String username, @QueryParam("password") final String password) {
        final Credentials credentials = new Credentials(username, password);
        try {
            final long now = System.currentTimeMillis();
            final String JWToken = authenticationService.authenticateUser(credentials);
            final Response response = Response.status(OK).header("Authorization", JWToken).build();
            statsdCommunicator.recordLatency(System.currentTimeMillis() - now);
            return response;
        } catch (final InvalidCredentialsException ie) {
            return Response.status(BAD_REQUEST).entity(ie.getMessage()).build();
        } catch (final Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }

    }

}
