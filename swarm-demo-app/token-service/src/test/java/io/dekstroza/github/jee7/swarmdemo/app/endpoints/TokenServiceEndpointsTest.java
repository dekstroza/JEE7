package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static org.fest.assertions.Assertions.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import main.Main;

@RunWith(Arquillian.class)
public class TokenServiceEndpointsTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1.0.0";

    @Deployment(testable = false)
    public static Archive createDeployment() throws Exception {
        return Main.createDeployment();
    }

    @RunAsClient
    @Test
    public void testHealthCheck() throws Exception {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/healthz");
            final Response response = target.request().get();
            assertThat(Response.Status.OK.getStatusCode()).isEqualTo(response.getStatus());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    @RunAsClient
    @Test
    public void testTokenGeneration() throws Exception {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/token");
            target.queryParam("username", "deki");
            target.queryParam("password", "deki");
            final Response response = target.request().get();
            assertThat(Response.Status.OK.getStatusCode()).isEqualTo(response.getStatus());
            assertThat(response.readEntity(String.class)).isNotEmpty();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

}
