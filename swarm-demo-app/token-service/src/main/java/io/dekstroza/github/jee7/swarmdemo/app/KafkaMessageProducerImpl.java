package io.dekstroza.github.jee7.swarmdemo.app;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

@ApplicationScoped
public class KafkaMessageProducerImpl implements KafkaMessageProducer {

    private final DatumWriter<RegistrationInfo> writer = new SpecificDatumWriter<RegistrationInfo>(RegistrationInfo.getClassSchema());

    private Producer<String, byte[]> producer;

    @PostConstruct
    private void initProducer() {
        this.producer = createProducer();
    }

    private Producer<String, byte[]> createProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka.default.svc.dekstroza.local:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        return new KafkaProducer<>(props);
    }

    @Override
    public DatumWriter<RegistrationInfo> getWriter() {
        return this.writer;
    }

    @Override
    public Producer<String, byte[]> getProducer() {
        return this.producer;
    }

    @PreDestroy
    private void deinit() {
        if (producer != null) {
            producer.close();
        }
    }

}
