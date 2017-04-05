package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.RegistrationServiceConfiguration;
import io.dekstroza.github.jee7.swarmdemo.app.domain.DomainPersistence;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import io.dekstroza.github.jee7.swarmdemo.app.domain.User;

@RequestScoped
@Path("v1.0.0")
public class RegistrationEndpoint {

    private static final Logger log = LoggerFactory.getLogger(RegistrationEndpoint.class);

    @Inject
    private DomainPersistence domainPersistence;

    @Inject
    private RegistrationServiceConfiguration serviceConfig;

    @Inject
    private Client client;

    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("register")
    @PUT
    public void register(final RegistrationInfo info, @Suspended AsyncResponse response) throws Exception {
        log.trace("registration request ->{}", info);

        //call user-service
        //call token-service
        //create and persist registration-info
        //return 201 with persisted registration info

        CompletableFuture<RegistrationInfo> cf = CompletableFuture.supplyAsync(new Supplier<RegistrationInfo>() {
            @Override
            public RegistrationInfo get() {
                return info;
            }
        });

        CompletableFuture<RegistrationInfo> c1 = cf.thenApplyAsync(this::callUserService);
        CompletableFuture<RegistrationInfo> c2 = cf.thenApplyAsync(this::callTokenService);
        CompletableFuture<RegistrationInfo> c3 = c1.thenCombine(c2, (registrationInfo, registrationInfo2) -> {
            info.setUserId(registrationInfo.getUserId());
            info.setAuthToken(registrationInfo2.getAuthToken());
            return info;
        });

        c3.thenApplyAsync(domainPersistence::persistRegistrationInfo).thenAccept(registrationInfo -> {
            response.resume(Response.status(Response.Status.CREATED).entity(registrationInfo).build());
        });

    }

    public RegistrationInfo callUserService(final RegistrationInfo info) {
        log.trace("user service request ->{}", info);
        Response response = null;
        User user = new User(info.getEmail(), info.getPassword());
        log.trace("Client is: {}", client);
        final WebTarget target = client.target(serviceConfig.getUserServiceURL() + "/users");
        try {
            response = target.request().put(Entity.entity(user, MediaType.APPLICATION_JSON));
            info.setUserId(response.readEntity(User.class).getId());
            return info;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public RegistrationInfo callTokenService(final RegistrationInfo info) {
        log.trace("token service request ->{}", info);
        Response response = null;
        try {
            log.trace("Client is: {}", client);
            final WebTarget target = client.target(serviceConfig.getTokenServiceURL() + "/token").queryParam("username", info.getEmail())
                    .queryParam("password", info.getPassword());
            log.trace("Token service target is:{}", target.getUri().toString());
            response = target.request().get();
            final String token = response.readEntity(String.class);
            info.setAuthToken(token);
            return info;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
