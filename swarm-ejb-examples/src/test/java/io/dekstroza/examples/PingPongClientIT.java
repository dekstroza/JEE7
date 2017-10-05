package io.dekstroza.examples;

import io.dekstroza.examples.io.dekstroza.examples.api.PingPongService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class PingPongClientIT {

    private static final Logger log = LoggerFactory.getLogger(PingPongClientIT.class);

    @Test
    public void testPingPongService() throws Exception {
        log.info("Testign ping pong service.");
        final Properties prop = new Properties();
        prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        prop.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        prop.put("jboss.naming.client.ejb.context", true);
        Context context = new InitialContext(prop);
        PingPongService pingPongService = (PingPongService) context.lookup(
                   "swarm-ejb-examples-1.0.1-SNAPSHOT/PingPongServiceImpl!io.dekstroza.examples.io.dekstroza.examples.api.PingPongService");
        Assert.assertNotNull(pingPongService);
        String result = pingPongService.pingPong("Hello there.");
        System.out.println("result = " + result);
        context.close();
    }
}
