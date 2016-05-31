package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.NoSuchApplicationUserException;
import io.dekstroza.github.jee7.swarmdemo.app.services.ApplicationUserService;

@Path("v1.0.0")
public class ApplicationUserRestEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationUserService.class);

    @EJB
    private ApplicationUserService applicationUserService;

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser")
    public Response findAllApplicationUsers() {
        final Collection<ApplicationUser> applicationUsers = applicationUserService.findAllApplicationUsers();
        return status(OK).entity(applicationUsers).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response insertApplicationUser(final ApplicationUser applicationUser) {
        try {
            final ApplicationUser persistedApplicationUser = applicationUserService.insertApplicationUser(applicationUser);
            return status(CREATED).header("Location", "http://localhost:8080/api/v1.0.0/applicationUser/" + persistedApplicationUser.getId()).build();
        } catch (final Exception e) {
            return status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser/{id}")
    public Response findApplicationUserById(@PathParam("id") final int id) {
        try {
            final ApplicationUser applicationUsers = applicationUserService.findApplicationUserById(id);
            return status(OK).entity(applicationUsers).build();
        } catch (final NoSuchApplicationUserException nae) {
            return status(Status.BAD_REQUEST).entity(nae.getMessage()).build();
        }
    }
}
