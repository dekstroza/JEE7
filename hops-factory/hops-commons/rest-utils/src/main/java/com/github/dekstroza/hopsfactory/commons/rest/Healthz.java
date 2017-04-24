package com.github.dekstroza.hopsfactory.commons.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;
import static javax.ws.rs.core.Response.status;

@Api(value = "/healthz", description = "Healthcheck endpoint.")
@RequestScoped
@Path("healthz")
@ExposeLogControl
public class Healthz {

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(Healthz.class);

    @ApiOperation(httpMethod = "GET", value = "Perform healt hcheck operation.", produces = TEXT_PLAIN, consumes = TEXT_PLAIN)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 503, message = "Health check failed.") })
    @GET
    @Consumes(TEXT_PLAIN)
    @Produces(TEXT_PLAIN)
    public Response basicHealthcheck() {

        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                log.debug("Healthcheck is returning OK.");
                return status(OK).entity("OK").build();
            } else {
                log.error("Healthcheck failed to return valid connection within 1 second.");
                return status(SERVICE_UNAVAILABLE).entity("Unable to get valid db connection").build();
            }
        } catch (SQLException sqlException) {
            log.error("Healthcheck returned sql exception.", sqlException);
            return status(SERVICE_UNAVAILABLE).entity(sqlException.getMessage()).build();
        }

    }
}
