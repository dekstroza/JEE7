package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.util.Date;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.domain.User;

@Stateless
@Path("v1.0.0")
public class UserEndpoint {

    private static final Logger log = LoggerFactory.getLogger(UserEndpoint.class);

    @PersistenceContext(unitName = "UsersPU")
    EntityManager entityManager;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users")
    @Asynchronous
    public void createUser(final User user, @Suspended final AsyncResponse response) {
        log.trace("Processing request: {}", user);
        try {
            user.setCreationDate(new Date());
            entityManager.persist(user);
            log.trace("Persisted user: {}", user);
            response.resume(status(CREATED).entity(user).build());
        } catch (Exception e) {
            log.error("Error creating user.", e);
            response.resume(status(BAD_REQUEST).entity(e).build());
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("users/{id}")
    @Asynchronous
    public void findUser(@PathParam("id") int id, @Suspended final AsyncResponse response) {
        try {
            final User user = entityManager.find(User.class, id);
            if (user != null) {
                response.resume(status(Response.Status.OK).entity(user).build());
            } else {
                response.resume(status(Response.Status.NOT_FOUND).build());
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
            response.resume(status(Response.Status.INTERNAL_SERVER_ERROR));
        }
    }

}
