package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.NoSuchApplicationUserException;
import io.dekstroza.github.jee7.swarmdemo.app.services.ApplicationUserService;

@Path("v1.0.0")
public class ApplicationUserRestEndpoint {

    @EJB
    private ApplicationUserService applicationUserService;

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser")
    public void findAllApplicationUsers(final @Suspended AsyncResponse response) {
        final Collection<ApplicationUser> applicationUsers = applicationUserService.findAllApplicationUsers();
        response.resume(status(OK).entity(applicationUsers).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public void insertApplicationUser(final ApplicationUser applicationUser, final @Suspended AsyncResponse response,
                                      final @Context UriInfo uriInfo) {
        try {
            final ApplicationUser persistedApplicationUser = applicationUserService.insertApplicationUser(applicationUser);
            final UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(persistedApplicationUser.getId()));
            response.resume(created(uriBuilder.build()).build());
        } catch (final Exception e) {
            response.resume(status(Status.BAD_REQUEST).entity(e.getMessage()).build());
        }

    }

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser/{id}")
    public void findApplicationUserById(@PathParam("id") final int id, final @Suspended AsyncResponse response) {
        try {
            final ApplicationUser applicationUsers = applicationUserService.findApplicationUserById(id);
            response.resume(status(OK).entity(applicationUsers).build());
        } catch (final NoSuchApplicationUserException nae) {
            response.resume(status(Status.BAD_REQUEST).entity(nae.getMessage()).build());
        }
    }
}
