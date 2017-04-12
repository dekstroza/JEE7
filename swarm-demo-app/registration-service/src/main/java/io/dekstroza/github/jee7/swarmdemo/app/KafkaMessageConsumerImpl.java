package io.dekstroza.github.jee7.swarmdemo.app;

import java.util.Arrays;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

@ApplicationScoped
public class KafkaMessageConsumerImpl implements KafkaMessageConsumer {

    private Consumer<String, byte[]> consumer;

    @PostConstruct
    private void initConsumer() {

        this.consumer = createConsumer();
        this.consumer.subscribe(Arrays.asList("registration-out"));
    }

    private KafkaConsumer<String, byte[]> createConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka.default.svc.dekstroza.local:9092");
        props.put("group.id", "registration-service");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        return consumer;

    }

    @Override
    public Consumer<String, byte[]> getConsumer() {
        return this.consumer;
    }

    @PreDestroy
    private void deinit() {
        if (consumer != null) {
            consumer.close();
        }
    }

}
