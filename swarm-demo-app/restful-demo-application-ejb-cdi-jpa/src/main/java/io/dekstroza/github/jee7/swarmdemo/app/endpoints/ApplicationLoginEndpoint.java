package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationConstants.*;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
    @Asynchronous
    public void login(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                      final @Suspended AsyncResponse response) {
        try {
            response.resume(status(OK).header(AUTHORIZATION, authenticate(new Credentials(username, password))).build());
        } catch (final InvalidCredentialsException ie) {
            response.resume(status(BAD_REQUEST).entity(ie.getMessage()).build());
        } catch (final Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).build());
        }

    }

    String authenticate(final Credentials credentials) throws InvalidCredentialsException {
        try {
            return generateToken(credentials).thenCombineAsync(findAppUser(credentials), (jwToken, appUser) -> {
                return jwToken;
            }).get();

        } catch (final CompletionException ce) {
            if (ce.getCause() instanceof NoResultException) {
                throw new InvalidCredentialsException(ce.getCause().getMessage());
            } else
                throw new RuntimeException(ce.getCause());
        } catch (final ExecutionException | InterruptedException exExc) {
            throw new RuntimeException(exExc.getMessage());
        }
    }

    CompletableFuture<String> generateToken(final Credentials credentials) {
        return new CompletableFuture<String>().supplyAsync(() -> {
            return credentials.generateJWToken();
        });

    }

    CompletableFuture<ApplicationUser> findAppUser(Credentials credentials) {
        return new CompletableFuture<ApplicationUser>().supplyAsync(() -> {
            return findApplicationUserByCredentials(credentials);
        });
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
