package rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketFactory implements Callable<Boolean> {

    public static final int N_THREADS = 20;
    private static final Logger log = LoggerFactory.getLogger(WebSocketFactory.class);
    private final CountDownLatch msgReceivedLatch;
    private final URI uri;
    private long latchAwait;
    private int sockNumber;
    private List<Future<ConsoleEndpoint>> resultList = new ArrayList<Future<ConsoleEndpoint>>();
    private TimeUnit timeUnit;

    /**
     * Constructor taking number of sockets, time to wait for connection to be estblished from all sockets and unit of time
     *
     * @param uri
     *            URI in format ws://someserver/somepath
     * 
     * @param sockNumber
     *            Number of sockets to create and connect to server
     * @param latchAwait
     *            Time to wait for all connections to be established
     * @param timeUnit
     *            Unit of time
     */
    public WebSocketFactory(final URI uri, final int sockNumber, final long latchAwait, final TimeUnit timeUnit) {
        this.latchAwait = latchAwait;
        this.timeUnit = timeUnit;
        this.sockNumber = sockNumber;
        this.uri = uri;
        try {
            this.msgReceivedLatch = new CountDownLatch(sockNumber);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create msg receive latch.");
        }

    }

    public Boolean call() throws URISyntaxException, InterruptedException {
        final CountDownLatch connectionCountLatch = new CountDownLatch(sockNumber);
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        for (int i = 0; i < sockNumber; i++) {
            final WebSocketConnector sockConnector = new WebSocketConnector(uri, connectionCountLatch, msgReceivedLatch);
            resultList.add(executorService.submit(sockConnector));
            log.info("Created socket {} .", i);
        }
        return connectionCountLatch.await(latchAwait, timeUnit);

    }

    /**
     * Close all sessions on this websocket factory.
     *
     * @throws Exception
     */
    public void closeAllSockets() throws Exception {
        for (Future<ConsoleEndpoint> futureEndpoint : resultList) {
            ConsoleEndpoint endpoint = futureEndpoint.get(2, TimeUnit.SECONDS);
            if (endpoint != null && endpoint.getSession() != null && endpoint.getSession().isOpen()) {
                endpoint.getSession().close();
            }
        }
    }

    public boolean msgReceivedByAllWebSockets() throws Exception {
        log.info("Waiting for all sockets to receive message");
        return this.msgReceivedLatch.await(latchAwait, timeUnit);
    }
}
