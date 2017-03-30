package io.dekstroza.github.jee7.swarmdemo.app;

import static io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationConstants.AUTHORIZATION;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TokenServiceEndpoint {

    private static final Logger log = LoggerFactory.getLogger(TokenServiceEndpoint.class);
    private String http_proxy_host;
    private Integer http_proxy_port;

    @PostConstruct
    void createTokenServiceEndpoint() {
        http_proxy_host = System.getenv("http_proxy_host");
        final String _http_proxy_port = System.getenv("http_proxy_port");
        http_proxy_port = Integer.parseInt(_http_proxy_port);
    }

    private ResteasyClient createRestEasyClient() {
        if (http_proxy_host != null && http_proxy_port != null) {
            return new ResteasyClientBuilder().defaultProxy(http_proxy_host, http_proxy_port).establishConnectionTimeout(2, TimeUnit.SECONDS)
                    .socketTimeout(5, TimeUnit.SECONDS).build();
        } else {
            return new ResteasyClientBuilder().establishConnectionTimeout(2, TimeUnit.SECONDS).socketTimeout(5, TimeUnit.SECONDS).build();
        }
    }

    public String getToken(final String username, final String password) {
        final ResteasyClient client = createRestEasyClient();
        try {
            WebTarget tokenEndpoint = client.target(System.getenv("token_service_url")).queryParam("username", username).queryParam("password",
                    password);
            final Response response = tokenEndpoint.request(MediaType.TEXT_PLAIN).get();
            final String token = response.getHeaderString(AUTHORIZATION);
            return token;

        } catch (Exception e) {
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}
