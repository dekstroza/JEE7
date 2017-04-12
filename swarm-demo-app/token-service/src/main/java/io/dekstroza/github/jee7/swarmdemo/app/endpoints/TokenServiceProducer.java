package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.KafkaMessageProducer;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Singleton
@TransactionAttribute(NOT_SUPPORTED)
public class TokenServiceProducer {

    private static final Logger log = LoggerFactory.getLogger(TokenServiceProducer.class);
    public static final String SUPER_SECRET_KEY = "mysupersecretkey";
    public static final String BEARER = "Bearer ";
    public static final String ISSUER = "https://dekstroza.io";
    private static final Calendar cal = Calendar.getInstance();

    private final DatumReader<RegistrationInfo> userDatumReader = new SpecificDatumReader<RegistrationInfo>(RegistrationInfo.class);

    @Inject
    private KafkaMessageProducer kafkaMessageProducer;

    @Asynchronous
    public void processIncommingMessage(final String key, final byte[] input) {
        try {
            Decoder decoder = DecoderFactory.get().binaryDecoder(input, null);
            final RegistrationInfo regInfo = userDatumReader.read(null, decoder);
            log.info("Processing registration info:{}", regInfo);
            regInfo.setAuthToken(generateJWToken(regInfo.getEmail().toString()));
            log.info("Sending new message on registration-out:{}", regInfo.toString());
            kafkaMessageProducer.getProducer().send(new ProducerRecord<String, byte[]>("reg-out", key, serializeRegistrationInfo(regInfo)));
        } catch (IOException ioe) {
            log.error("Error processing message.", ioe);
        }
    }

    private byte[] serializeRegistrationInfo(final RegistrationInfo regInfo) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        kafkaMessageProducer.getWriter().write(regInfo, encoder);
        encoder.flush();
        out.close();
        return out.toByteArray();
    }

    private String generateJWToken(final String username) {
        final Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        final String jwtToken = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(username).setIssuedAt(now).setIssuer(ISSUER)
                .setExpiration(cal.getTime()).signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
        return new StringBuilder(BEARER).append(jwtToken).toString();
    }

}
