package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.KafkaMessageProducer;
import io.dekstroza.github.jee7.swarmdemo.app.RegistrationServiceConfiguration;
import io.dekstroza.github.jee7.swarmdemo.app.domain.DomainPersistence;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfoEntity;

@RequestScoped
@Path("v1.0.0")
public class RegistrationEndpoint {

    private static final Logger log = LoggerFactory.getLogger(RegistrationEndpoint.class);

    @Inject
    private DomainPersistence domainPersistence;

    @Inject
    private RegistrationServiceConfiguration serviceConfig;

    @Inject
    private KafkaMessageProducer kafkaSender;

    @Inject
    private Client client;

    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("register")
    @PUT
    public void register(final RegistrationInfoEntity registrationInfo, @Suspended AsyncResponse response) throws Exception {
        log.trace("registration request ->{}", registrationInfo);
        registrationInfo.setId(UUID.randomUUID().toString());
        kafkaSender.getProducer().send(new ProducerRecord<String, byte[]>("reg-in", registrationInfo.getId(),
                kafkaSender.serializeRegistrationInfo(createKafkaMessage(registrationInfo))));
        response.resume(status(CREATED).entity(registrationInfo).build());
    }

    private RegistrationInfo createKafkaMessage(RegistrationInfoEntity infoEntity) {
        final RegistrationInfo info = RegistrationInfo.newBuilder().build();
        info.setId(infoEntity.getId());
        info.setPassword(infoEntity.getPassword());
        info.setEmail(infoEntity.getEmail());
        return info;
    }

}
