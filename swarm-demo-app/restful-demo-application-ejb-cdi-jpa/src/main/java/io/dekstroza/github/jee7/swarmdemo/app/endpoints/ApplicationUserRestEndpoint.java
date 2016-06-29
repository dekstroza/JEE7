package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;

import java.util.Collection;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import io.dekstroza.github.jee7.swarmdemo.app.api.AbstractApplicationUserRestEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.NoSuchApplicationUserException;

@Path("v1.0.0")
@Stateless
public class ApplicationUserRestEndpoint extends AbstractApplicationUserRestEndpoint {

    @PersistenceContext
    private EntityManager em;

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser")
    @Asynchronous
    public void findAllApplicationUsers(final @Suspended AsyncResponse response) {
        final Collection<ApplicationUser> applicationUsers = findAllApplicationUsers();
        response.resume(status(OK).entity(applicationUsers).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Asynchronous
    public void insertApplicationUser(final ApplicationUser applicationUser, final @Suspended AsyncResponse response,
                                      final @Context UriInfo uriInfo) {
        super.insertApplicationUser(applicationUser, response, uriInfo);
    }

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser/{id}")
    @Asynchronous
    public void findApplicationUserById(@PathParam("id") final int id, final @Suspended AsyncResponse response) {
        try {
            final ApplicationUser applicationUsers = findApplicationUserById(id);
            response.resume(status(OK).entity(applicationUsers).build());
        } catch (final NoSuchApplicationUserException nae) {
            response.resume(status(BAD_REQUEST).entity(nae.getMessage()).build());
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

}
