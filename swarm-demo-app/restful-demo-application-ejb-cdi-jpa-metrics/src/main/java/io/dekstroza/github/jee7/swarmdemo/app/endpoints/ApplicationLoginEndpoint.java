package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationConstants.*;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.dekstroza.github.jee7.swarmdemo.app.ProfilingInterceptor;
import io.dekstroza.github.jee7.swarmdemo.app.api.AbstractApplicationLoginEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.InvalidCredentialsException;

@Stateless
@Path("v1.0.0")
public class ApplicationLoginEndpoint extends AbstractApplicationLoginEndpoint {

    @PersistenceContext
    private EntityManager em;

    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Interceptors(ProfilingInterceptor.class)
    @Asynchronous
    public void applicationLoginFunct(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                                      final @Suspended AsyncResponse response) {

        try {
            final Credentials credentials = new Credentials(username, password);
            final String JWToken = authenticateUser(credentials);
            response.resume(status(OK).header(AUTHORIZATION, JWToken).build());
        } catch (final InvalidCredentialsException ie) {
            response.resume(status(BAD_REQUEST).entity(ie.getMessage()).build());
        } catch (final Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).build());
        }

    }

    protected ApplicationUser findApplicationUserByCredentials(final Credentials credentials) {
        return em.createQuery("SELECT au FROM ApplicationUser au WHERE au.username = :username AND au.password = :password", ApplicationUser.class)
                .setParameter(USERNAME, credentials.getUsername()).setParameter(PASSWORD, credentials.getPassword()).getSingleResult();
    }

}
