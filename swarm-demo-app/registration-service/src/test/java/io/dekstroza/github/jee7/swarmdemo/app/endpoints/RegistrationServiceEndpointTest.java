package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.Assertions.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import main.Main;

@RunWith(Arquillian.class)
public class RegistrationServiceEndpointTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1.0.0";

    @Deployment(testable = false)
    public static Archive createDeployment() throws Exception {
        return Main.createDeployment();
    }


    @RunAsClient
    @Test
    public void testHealthzEndpoint_WHEN_Healthly() throws Exception {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/healthz");
            final Response response = target.request().get();
            assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    @RunAsClient
    @Test
    public void testCreateUser() throws Exception {
        final RegistrationInfo info = new RegistrationInfo();
        info.setEmail("kdejan@gmail.com");
        info.setPassword("password");
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/register");
            final Response response = target.request().put(Entity.entity(info, MediaType.APPLICATION_JSON));
            final RegistrationInfo responseUser = response.readEntity(RegistrationInfo.class);
            assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

}
