package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dekstroza.github.jee7.swarmdemo.app.RegistrationServiceConfiguration;

@RequestScoped
@Path("v1.0.0")
public class HealthzEndpoint {

    @Inject
    RegistrationServiceConfiguration serviceConfiguration;

    @Produces(MediaType.TEXT_PLAIN)
    @Path("healthz")
    @GET
    public Response healthCheck() {

        if (!isUserServiceURLDefined()) {
            return status(SERVICE_UNAVAILABLE).entity("USER SERVICE URL IS INVALID").build();
        }
        if (!isTokenServiceURLDefined()) {
            return status(SERVICE_UNAVAILABLE).entity("TOKEN SERVICE URL IS INVALID").build();

        }
        return status(OK).entity("OK").build();
    }

    private boolean isTokenServiceURLDefined() {
        if (serviceConfiguration.getTokenServiceURL() == null || serviceConfiguration.getTokenServiceURL().isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isUserServiceURLDefined() {
        if (serviceConfiguration.getUserServiceURL() == null || serviceConfiguration.getUserServiceURL().isEmpty()) {
            return false;
        }
        return true;
    }

}
