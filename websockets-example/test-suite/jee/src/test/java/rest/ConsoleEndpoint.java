package rest;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.websocket.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleEndpoint extends Endpoint {

    private static final Logger log = LoggerFactory.getLogger(ConsoleEndpoint.class);
    private final CountDownLatch connectionCountLatch;
    private final CountDownLatch msgReceivedLatch;
    private Session session;

    /**
     * Constructor taking URI of websocket server and two latches, one for message received and another for connection count latch.
     * 
     * @param uri
     *            URI of the websocket server
     * @param connectionCountLatch
     *            Latch counting down number of connections established.
     */
    public ConsoleEndpoint(final URI uri, final CountDownLatch connectionCountLatch, final CountDownLatch msgReceivedLatch) {
        log.debug("ConsoleEndpoint constructor called for uri={}", uri);
        final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.connectionCountLatch = connectionCountLatch;
        this.msgReceivedLatch = msgReceivedLatch;
        try {
            log.trace("About to call connectToServer with uri=[{}]", uri);
            this.session = container.connectToServer(this, uri);
            log.trace("After call to connect");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return session associated with this endpoint
     * 
     * @return Session handle for this endpoint
     */
    public Session getSession() {
        return session;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        session.addMessageHandler(new ConsoleEndpointMessageHandler(msgReceivedLatch));
        try {
            while (!session.isOpen()) {
                log.debug("Waiting for session {} to open", session.getId());
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionCountLatch.countDown();
        log.debug("After latch countdown, connection latch count is:{}", connectionCountLatch.getCount());
    }

    @Override
    public void onError(Session session, Throwable thr) {
        super.onError(session, thr);
        log.error("Error caught:", thr);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
        log.debug("Closing session {}, reason is:{}", session.getId(), closeReason.getCloseCode().getCode());
    }
}
