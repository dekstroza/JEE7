package io.dekstroza.github.jee7.swarmdemo.app.api;

import static java.lang.String.format;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.status;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public abstract class AbstractApplicationUserRestEndpoint {

    protected void insertApplicationUser(final ApplicationUser applicationUser, final @Suspended AsyncResponse response,
                                         final @Context UriInfo uriInfo) {
        try {
            final ApplicationUser persistedApplicationUser = persistApplicationUser(applicationUser);
            final UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(persistedApplicationUser.getId()));
            response.resume(created(uriBuilder.build()).build());
        } catch (final Exception e) {
            response.resume(status(BAD_REQUEST).entity(e.getMessage()).build());
        }

    }

    protected abstract EntityManager getEntityManager();

    protected Collection<ApplicationUser> findAllApplicationUsers() {
        Collection<ApplicationUser> applicationUsers = getEntityManager().createQuery("SELECT au FROM ApplicationUser au", ApplicationUser.class)
                .getResultList();
        return applicationUsers;
    }

    protected ApplicationUser findApplicationUserById(int id) throws NoSuchApplicationUserException {
        try {
            return getEntityManager().find(ApplicationUser.class, id);
        } catch (final NoResultException nre) {
            throw new NoSuchApplicationUserException(format("Application user with id=%s does not exist", id));
        }
    }

    protected ApplicationUser persistApplicationUser(final ApplicationUser applicationUser) {
        getEntityManager().persist(applicationUser);
        return applicationUser;
    }
}
