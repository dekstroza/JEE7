package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.KafkaMessageConsumer;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;

@Startup
@TransactionAttribute(NOT_SUPPORTED)
@Singleton
public class RegistrationServiceConsumer {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceConsumer.class);

    @Inject
    private KafkaMessageConsumer kafkaMessageConsumer;

    @Inject
    private RegistrationInfoProcessor processor;

    @Resource
    private ManagedExecutorService mes;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    @PostConstruct
    private void init() {
        log.info("Starting subscriber.");
        startSubscriber();
    }

    @PreDestroy
    private void deinit() {
        log.info("Closing subscriber.");
        closed.set(true);
        kafkaMessageConsumer.getConsumer().wakeup();
    }

    public void startSubscriber() {
        mes.execute(() -> {
            try {
                kafkaMessageConsumer.getConsumer().subscribe(Arrays.asList("reg-out"));
                RegistrationInfo regInfo = new RegistrationInfo();
                while (!closed.get()) {
                    ConsumerRecords<String, byte[]> records = kafkaMessageConsumer.getConsumer().poll(10000);
                    for (ConsumerRecord<String, byte[]> record : records) {
                        processor.processIncommingMessage(record.key(), record.value());
                    }
                }
            } catch (WakeupException e) {
                if (!closed.get())
                    throw e;
            } finally {
                {
                    kafkaMessageConsumer.getConsumer().close();
                }
            }
        }

        );
    }

}
