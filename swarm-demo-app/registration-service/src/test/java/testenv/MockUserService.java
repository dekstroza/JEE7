package testenv;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.util.Date;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.dekstroza.github.jee7.swarmdemo.app.domain.User;

@Stateless
@Path("v1.0.0")
public class MockUserService {

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    @Path("users")
    @Asynchronous
    public void createUser(final User user, @Suspended final AsyncResponse response) {
        user.setId(1234);
        user.setCreationDate(new Date());
        response.resume(status(CREATED).entity(user).build());
    }
}
