package org.dekstroza.swarm.application.security.endpoint;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dekstroza.swarm.application.security.SecurityService;
import org.slf4j.Logger;

import com.dekstroza.swarm.security.entities.User;

@Path("v1.0")
public class SecurityRestEndpoint {

    @Inject
    private Logger logger;

    @Inject
    private SecurityService securityService;

    @GET
    @Path("security/user/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findUser(@PathParam("username") final String username) {
        try {
            return securityService.findUserByUsername(username);
        } catch (final Exception anyException) {
            return null;
        }
    }

    @POST
    @Path("security/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertNewUser(@NotNull final User user) {

        try {
            securityService.insertUser(user);
            return Response.status(Response.Status.OK).entity(new OperationResult(OperationStatus.SUCCESS, "User added.")).build();
        } catch (final Exception anyException) {
            return Response.status(Response.Status.OK).entity(new OperationResult(OperationStatus.ERROR, anyException.getCause().getMessage()))
                    .build();
        }

    }

    @GET
    @Path("security/user/{username}/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@PathParam("username") final String username, @NotNull @QueryParam("password") final String password) {

        try {
            final User user = securityService.findUserByUsername(username);
            if (user != null && password.equals(user.getPassword())) {
                return Response.status(Response.Status.OK).entity(new OperationResult(OperationStatus.SUCCESS, "Login Successful.")).build();
            } else {
                return Response.status(Response.Status.OK).entity(new OperationResult(OperationStatus.ERROR, "Invalid username or password."))
                        .build();
            }
        } catch (final Exception anyException) {
            return Response.status(Response.Status.OK).entity(new OperationResult(OperationStatus.ERROR, anyException.getCause().getMessage()))
                    .build();
        }

    }

    private class OperationResult {

        private final OperationStatus status;
        private final String message;

        public OperationResult(final OperationStatus status, final String message) {
            this.status = status;
            this.message = message;
        }

        public OperationStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    private enum OperationStatus {
        SUCCESS, ERROR
    }

}
