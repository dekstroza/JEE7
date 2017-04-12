package io.dekstroza.github.jee7.swarmdemo.app;

import org.apache.kafka.clients.consumer.Consumer;

public interface KafkaMessageConsumer {

    Consumer<String, byte[]> getConsumer();
}
