package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.TokenServiceEndpoint;

@Stateless
@Path("v1.0.0")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class UserRegistrationEndpoint {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationEndpoint.class);

    @Inject
    TokenServiceEndpoint tokenService;

    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("public/data")
    public Response showSomePublicData() {
        return status(OK).entity("Some public data version.").build();
    }

    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("public/register_user")
    @Asynchronous
    public void registerUser(final @QueryParam("username") String username, final @QueryParam("password") String password,
                             @Suspended final AsyncResponse response) {
        try {
            response.resume(status(OK).entity(tokenService.getToken(username, password)).build());
        } catch (Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e).build());
        }

    }

}
