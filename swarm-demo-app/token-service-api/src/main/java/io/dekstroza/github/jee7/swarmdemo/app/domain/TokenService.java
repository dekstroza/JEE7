package io.dekstroza.github.jee7.swarmdemo.app.domain;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface TokenService {

    String USERNAME = "username";
    String PASSWORD = "password";

    @Path("token")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String generateToken(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password);
}
