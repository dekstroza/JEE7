package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import java.io.IOException;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfoEntity;

@Stateless
public class RegistrationInfoProcessor {

    private static final Logger log = LoggerFactory.getLogger(RegistrationInfoProcessor.class);
    private final DatumReader<RegistrationInfo> userDatumReader = new SpecificDatumReader<RegistrationInfo>(RegistrationInfo.class);

    @PersistenceContext(unitName = "RegistrationsPU")
    EntityManager entityManager;

    @Asynchronous
    public void processIncommingMessage(final String key, final byte[] message) {
        try {
            Decoder decoder = DecoderFactory.get().binaryDecoder(message, null);
            final RegistrationInfo regInfo = userDatumReader.read(null, decoder);
            final RegistrationInfoEntity entity = new RegistrationInfoEntity();
            entity.setAuthToken(regInfo.getAuthToken().toString());
            entity.setId(regInfo.getId().toString());
            entity.setEmail(regInfo.getEmail().toString());
            entity.setPassword(regInfo.getPassword().toString());
            entity.setUserId(regInfo.getUserId());
            entityManager.persist(entity);
        } catch (IOException ioe) {
            log.error("Error processing message.", ioe);
        }
    }
}
