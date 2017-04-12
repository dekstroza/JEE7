package io.dekstroza.github.jee7.swarmdemo.app;

import org.apache.avro.io.DatumWriter;
import org.apache.kafka.clients.producer.Producer;

import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;

public interface KafkaMessageProducer {

    Producer<String, byte[]> getProducer();

    DatumWriter<RegistrationInfo> getWriter();

}
