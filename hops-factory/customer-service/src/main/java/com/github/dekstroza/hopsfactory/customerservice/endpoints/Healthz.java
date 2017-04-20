package com.github.dekstroza.hopsfactory.customerservice.endpoints;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.customerservice.ExposeLogControl;

@RequestScoped
@Path("healthz")
public class Healthz implements ExposeLogControl {

    @Resource(lookup = "jboss/datasources/CustomerDS")
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(Healthz.class);

    @GET
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
