package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1.0.0")
public class HealthzEndpoint {

    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("public/healthz")
    public Response healthCheck() {
        final String http_proxy_host = System.getenv("http_proxy_host");
        final String http_proxy_port = System.getenv("http_proxy_port");

        if (System.getenv("token_service_url") == null || System.getenv("token_service_url").isEmpty()) {
            return status(INTERNAL_SERVER_ERROR).entity("environment variable [token_service_url] is not defined.").build();
        }

        if (http_proxy_host != null && http_proxy_host.isEmpty()) {
            return status(INTERNAL_SERVER_ERROR).entity("environment variable [http_proxy_host] can not be empty.").build();
        }

        if (http_proxy_port != null) {
            try {
                Integer.parseInt(http_proxy_port);
            } catch (NumberFormatException nfe) {
                return status(INTERNAL_SERVER_ERROR).entity("environment variable [http_proxy_port] is not valid number.").build();
            }
        }

        return status(OK).build();
    }
}
