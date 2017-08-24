package com.github.dekstroza.jee;

import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

@Path("/sender")
public class SenderApplication extends Application {

    @Inject
    @JMSConnectionFactory("java:/jms/remote-mq")
    private JMSContext context;

    @GET
    @Produces("text/plain")
    public String get() {
        context.createProducer().send(context.createQueue("clustered-mediation-queue"), "Hello!");
        return "Howdy!";
    }
}
