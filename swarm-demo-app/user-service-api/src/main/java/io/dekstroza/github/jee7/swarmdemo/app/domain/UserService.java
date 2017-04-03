package io.dekstroza.github.jee7.swarmdemo.app.domain;

import javax.ejb.Asynchronous;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface UserService {

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    @Path("users")
    @Asynchronous
    public User createUser(final User user);

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("users/{id}")
    @Asynchronous
    public User findUser(@PathParam("id") int id);
}
