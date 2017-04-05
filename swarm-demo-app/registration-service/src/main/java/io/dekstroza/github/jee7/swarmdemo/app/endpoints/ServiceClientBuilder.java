package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.client.Client;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.RegistrationServiceConfiguration;

@ApplicationScoped
public class ServiceClientBuilder {

    private static final Logger log = LoggerFactory.getLogger(ServiceClientBuilder.class);
    private ResteasyClientBuilder resteasyClientBuilder;
    private Client client;

    @Inject
    private RegistrationServiceConfiguration serviceConfig;

    @Resource
    private ManagedExecutorService mes;

    @PostConstruct
    private void init() {
        if (serviceConfig.getHttpProxyHost() != null && serviceConfig.getHttpProxyPort() != null) {
            log.trace("Building rest client with proxy[{}:{}", serviceConfig.getHttpProxyHost(), serviceConfig.getHttpProxyPort());
            this.resteasyClientBuilder = new ResteasyClientBuilder()
                    .defaultProxy(serviceConfig.getHttpProxyHost(), Integer.parseInt(serviceConfig.getHttpProxyPort())).connectionPoolSize(50)
                    .establishConnectionTimeout(5, TimeUnit.SECONDS).socketTimeout(5, TimeUnit.SECONDS).asyncExecutor(mes);
        } else {
            log.trace("Building rest client without proxy!");
            this.resteasyClientBuilder = new ResteasyClientBuilder().establishConnectionTimeout(5, TimeUnit.SECONDS)
                    .socketTimeout(5, TimeUnit.SECONDS).connectionPoolSize(50).asyncExecutor(mes);
        }

        this.client = resteasyClientBuilder.build();

    }

    @Produces
    public Client getResteasyClient() {
        return this.client;
    }

}
