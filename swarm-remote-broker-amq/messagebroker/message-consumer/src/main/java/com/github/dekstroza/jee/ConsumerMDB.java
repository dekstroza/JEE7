package com.github.dekstroza.jee;

import org.jboss.ejb3.annotation.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

@ResourceAdapter("remote-mq")
@MessageDriven(name = "MediationQueueMDB", activationConfig = {
           @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "/jms/queue/mediation-queue"),
           @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), })
public class ConsumerMDB implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(ConsumerMDB.class);

    public void onMessage(Message message) {

        try {
            log.info("Consumed message: {}", ((TextMessage) message).getText());
        } catch (Exception e) {
            log.error("Error consuming message:", e);
        }
    }
}
