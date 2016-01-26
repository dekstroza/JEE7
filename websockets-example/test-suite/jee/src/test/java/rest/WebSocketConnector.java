package rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runnable websocket connector
 */
public class WebSocketConnector implements Callable<ConsoleEndpoint> {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConnector.class);
    private final CountDownLatch connectionCountLatch;
    private final CountDownLatch msgReceivedLatch;
    private final URI uri;

    /**
     * Constructor taking connnection count latch as arguments.
     * 
     * @param connectionCountLatch
     *            Latch counting down number of established connections.
     */
    public WebSocketConnector(final URI uri, final CountDownLatch connectionCountLatch, final CountDownLatch msgReceivedLatch) {
        this.connectionCountLatch = connectionCountLatch;
        this.msgReceivedLatch = msgReceivedLatch;
        this.uri = uri;
    }

    /**
     * Call method for Callable interface
     * 
     * @return ConsoleEndpoint for created websocket endpoint
     * @throws URISyntaxException
     */
    public ConsoleEndpoint call() throws URISyntaxException {
        log.trace("Creating websocket endpoint.");
        return new ConsoleEndpoint(uri, connectionCountLatch, msgReceivedLatch);

    }
}
