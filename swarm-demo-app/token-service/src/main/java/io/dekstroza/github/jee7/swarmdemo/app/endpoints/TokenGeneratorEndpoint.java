package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.domain.Credentials;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@TransactionAttribute(NOT_SUPPORTED)
@Stateless
@Path("v1.0.0")
@Api(value = "/token", description = "Generate authentication token for provided user credentials", tags = "token")
public class TokenGeneratorEndpoint {

    private final Logger log = LoggerFactory.getLogger(TokenGeneratorEndpoint.class);
    private final String PASSWORD = "password";
    private final String USERNAME = "username";

    @Path("token")
    @GET
    @Produces(TEXT_PLAIN)
    @Asynchronous
    @ApiOperation(value = "Create authentication token", notes = "Returns JWT token as string", response = String.class)
    public void generateToken(@ApiParam(name = "username", value = "Username, in the form of email address", required = true, example = "darla@mrrobot.io") @QueryParam(USERNAME) final String username,
                              @ApiParam(name = "password", value = "Password for this user", required = true, example = "abcdefgh") @QueryParam(PASSWORD) final String password,
                              @Suspended final AsyncResponse response) {
        log.trace("Processing request for username={} and password={}", username, password);
        final String jwtToken = new Credentials(username, password).generateJWToken();
        log.trace("Generated token: {}", jwtToken);
        response.resume(status(OK).entity(jwtToken).build());
    }

}
