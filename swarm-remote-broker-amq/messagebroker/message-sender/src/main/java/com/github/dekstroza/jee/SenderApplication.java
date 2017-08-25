package com.github.dekstroza.jee;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

@Path("/sender")
public class SenderApplication extends Application {

    @Inject
    @JMSConnectionFactory("java:/jms/remote-mq")
    private JMSContext context;

    @Resource(mappedName = "jms/queue/mediation-queue")
    private Queue queue;

    @GET
    @Produces("text/plain")
    public String get() {
        context.createProducer().send(queue, "Hello!");
        return "Howdy!";
    }
}
