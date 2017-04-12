package io.dekstroza.github.jee7.swarmdemo.app;

import java.io.IOException;

import org.apache.kafka.clients.producer.Producer;

import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;

public interface KafkaMessageProducer {

    Producer<String, byte[]> getProducer();

    byte[] serializeRegistrationInfo(final RegistrationInfo regInfo) throws IOException;
}
