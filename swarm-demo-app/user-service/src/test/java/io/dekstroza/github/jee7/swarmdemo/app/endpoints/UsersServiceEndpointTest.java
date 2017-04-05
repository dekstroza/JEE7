package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.Status.*;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fest.assertions.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.dekstroza.github.jee7.swarmdemo.app.domain.User;
import main.Main;

@RunWith(Arquillian.class)
public class UsersServiceEndpointTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1.0.0";

    @Deployment(testable = false)
    public static Archive createDeployment() throws Exception {
        return Main.createDeployment();
    }

    @RunAsClient
    @Test
    public void testHealthzEndpoint_WHEN_TokenService_Env_NOT_Defined() throws Exception {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/healthz");
            final Response response = target.request().get();
            Assertions.assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    //TODO: Fix this to pass
    @Ignore
    @RunAsClient
    @Test
    public void testHealthzEndpoint_WHEN_TokenService_Env_Defined() throws Exception {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/healthz");
            final Response response = target.request().get();
            Assertions.assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    @InSequence(1)
    @RunAsClient
    @Test
    public void testCreateUser() throws Exception {
        final User user = new User();
        user.setEmail("dejan.kitic@ericsson.com");
        user.setPassword("kolokvijum");
        user.setCreationDate(new Date());
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/users");
            final Response response = target.request().put(Entity.entity(user, MediaType.APPLICATION_JSON));
            final User responseUser = response.readEntity(User.class);
            assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
            assertThat(responseUser.getId()).isEqualTo(1);
            assertThat(responseUser.getEmail()).isEqualTo(user.getEmail());
            assertThat(responseUser.getPassword()).isEqualTo(user.getPassword());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    @InSequence(2)
    @RunAsClient
    @Test
    public void testGetUser() throws Exception {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/users/1");
            final Response response = target.request().get();
            Assertions.assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

}