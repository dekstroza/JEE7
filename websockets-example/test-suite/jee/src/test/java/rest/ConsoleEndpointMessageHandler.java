package rest;

import java.util.concurrent.CountDownLatch;

import javax.websocket.MessageHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message handler for websocket endpoint
 */
public class ConsoleEndpointMessageHandler implements MessageHandler.Whole<String> {

    private static final Logger log = LoggerFactory.getLogger(ConsoleEndpointMessageHandler.class);
    private final CountDownLatch msgReceivedLatch;

    /**
     * Full argument constructor
     * 
     * @param msgReceivedLatch
     *            CountdownLatch used to countdown received messages.
     */
    public ConsoleEndpointMessageHandler(final CountDownLatch msgReceivedLatch) {
        this.msgReceivedLatch = msgReceivedLatch;
    }

    public void onMessage(final String s) {
        log.trace("Message received is:{}", s);
        this.msgReceivedLatch.countDown();
        log.trace("Current latch count is:{}", this.msgReceivedLatch.getCount());
    }
}
