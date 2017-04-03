package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.domain.*;

@Stateless
@Path("v1.0.0")
public class RegistrationEndpoint {

    private static final Logger log = LoggerFactory.getLogger(RegistrationEndpoint.class);

    @Inject
    private DomainPersistence domainPersistence;

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
            log.trace("registration info persisted ->{}", registrationInfo);
            response.resume(Response.status(Response.Status.CREATED).entity(registrationInfo).build());
        });

    }

    public RegistrationInfo callUserService(final RegistrationInfo info) {
        log.info("calling user service");
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://user-service/api/v1.0.0/");
        UserService userService = target.proxy(UserService.class);
        User user = new User(info.getEmail(), info.getPassword());
        user = userService.createUser(user);
        log.info("called user service, returned user is {}", user);
        return info;
    }

    public RegistrationInfo callTokenService(final RegistrationInfo info) {
        log.info("calling token service");
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://token-service/api/v1.0.0/");
        TokenService tokenService = target.proxy(TokenService.class);
        info.setAuthToken(tokenService.generateToken(info.getEmail(), info.getPassword()));
        log.info("called token service");
        return info;
    }

}
