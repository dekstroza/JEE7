package io.dekstroza.github.jee7.swarmdemo.app.api;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.status;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public abstract class AbstractApplicationUserRestEndpoint {

    protected void insertApplicationUser(final ApplicationUser applicationUser, final @Suspended AsyncResponse response,
                                         final @Context UriInfo uriInfo) {
        try {
            final ApplicationUser persistedApplicationUser = insertApplicationUser(applicationUser);
            final UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(persistedApplicationUser.getId()));
            response.resume(created(uriBuilder.build()).build());
        } catch (final Exception e) {
            response.resume(status(BAD_REQUEST).entity(e.getMessage()).build());
        }

    }

    protected abstract ApplicationUser insertApplicationUser(final ApplicationUser applicationUser);
}
