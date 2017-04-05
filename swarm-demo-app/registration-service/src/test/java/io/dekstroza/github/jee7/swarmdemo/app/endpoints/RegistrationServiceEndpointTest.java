package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.Assertions.assertThat;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import io.dekstroza.github.jee7.swarmdemo.app.RegistrationServiceConfigurationImpl;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import main.Main;
import testenv.ArquillianRegistrationServiceConfigurationImpl;
import testenv.MockTokenService;
import testenv.MockUserService;

@RunWith(Arquillian.class)
public class RegistrationServiceEndpointTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1.0.0";

    @Deployment(testable = false)
    public static Archive createDeployment() throws Exception {
        JAXRSArchive deployment = Main.createDeployment();
        deployment.deleteClass(RegistrationServiceConfigurationImpl.class);
        deployment.addClass(ArquillianRegistrationServiceConfigurationImpl.class);
        deployment.addClass(MockUserService.class);
        deployment.addClass(MockTokenService.class);
        return deployment;
    }

    @RunAsClient
    @Test
    public void testHealthzEndpoint_WHEN_Healthly() throws Exception {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(BASE_URL + "/healthz");
            final Response response = target.request().get();
            System.out.println("response = " + response.readEntity(String.class));
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
        System.out.println("Executing testCreateUser.");
        final RegistrationInfo info = new RegistrationInfo();
        info.setEmail("kdejan@gmail.com");
        info.setPassword("password");
        final Client client = ClientBuilder.newClient();
        try {

            final WebTarget target = client.target(BASE_URL + "/register");
            System.out.println("About to call http put.");
            try {
                final Entity<RegistrationInfo> entity = Entity.entity(info, APPLICATION_JSON);
                Invocation.Builder builder = target.request();
                final Response response = builder.put(entity);
                System.out.println("Response received.");
                final RegistrationInfo responseUser = response.readEntity(RegistrationInfo.class);
                System.out.println("Response body is:"+ responseUser);
                assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

}
