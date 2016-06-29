package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationConstants.*;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.dekstroza.github.jee7.swarmdemo.app.api.AbstractApplicationLoginEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;

@Stateless
@Path("v1.0.0")
public class ApplicationLoginEndpoint extends AbstractApplicationLoginEndpoint {

    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Asynchronous
    public void applicationLogin(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                                 @Suspended final AsyncResponse response) {
        final String jwtToken = new Credentials(username, password).generateJWToken();
        response.resume(status(OK).header(AUTHORIZATION, jwtToken).build());
    }

    @Override
    protected ApplicationUser findApplicationUserByCredentials(Credentials credentials) {
        throw new IllegalStateException("Method not supported.");
    }

    @Override
    protected EntityManager getEntityManager() {
        throw new IllegalStateException("Not supported.");
    }
}
