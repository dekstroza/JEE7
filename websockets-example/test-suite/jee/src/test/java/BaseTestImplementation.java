import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rest.WebSocketFactory;
import confenv.SimpleConfigurationEnvironment;

public abstract class BaseTestImplementation {

    private static final Logger log = LoggerFactory.getLogger(BaseTestImplementation.class);

    /**
     * Number of thread for executor service, when creating web sockets.
     */
    private static final int MAX_THREAD = 4;

    protected abstract WebSocketFactory getWebSocketFactory();

    protected abstract int getSocketNumber();

    protected abstract long getSocketCreateTimeout();

    public static WebArchive createDeployment(final String name) {
        WebArchive war = ShrinkWrap.create(WebArchive.class, name);
        war.addPackage("org.dekstroza.websockets.partitioning.api");
        war.addPackage("org.dekstroza.websockets.configuration.api");
        war.addPackage("org.dekstroza.websockets.rest");
        war.addPackage("org.dekstroza.websockets.session");
        war.addPackage("org.dekstroza.websockets.util.logging");
        war.addClass(SimpleConfigurationEnvironment.class);
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return war;
    }

    protected void createWebsocketClients() throws Exception {
        final long start = System.nanoTime();
        Assert.assertTrue(createWebSockets(getWebSocketFactory()).get(getSocketCreateTimeout(), TimeUnit.SECONDS));
        final long end = System.nanoTime();
        log.info("Created {} websocket(s) in {}ms.", getSocketNumber(), String.format("%.3f", ((double) (end - start)) / 1000000));
    }

    protected void closeWebSocketClients() throws Exception {
        getWebSocketFactory().closeAllSockets();
        log.info("Closed all sockets...");
    }

    protected void singleRequestToMultipleClients(final String REQUEST_URL, final String QUERY_PARAM_NAME, final String QUERY_PARAM_VALUE)
            throws Exception {

        log.info("Invoking rest interface with single log message.", getSocketNumber());
        sendSingleRequest(REQUEST_URL, QUERY_PARAM_NAME, QUERY_PARAM_VALUE);
        log.info("Waiting for all websockets to confirm message delivery.");
        final long start = System.nanoTime();
        final boolean isConfirmed = getWebSocketFactory().msgReceivedByAllWebSockets();
        final long end = System.nanoTime();
        Assert.assertTrue(isConfirmed);
        log.info("Time taken to deliver single message to {} client(s) was: {}ms.", getSocketNumber(),
                String.format("%.3f", ((double) end - start) / 1000000));
        getWebSocketFactory().closeAllSockets();

    }

    protected static Future<Boolean> createWebSockets(final WebSocketFactory factory) {
        final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD);
        return executorService.submit(factory);
    }

    protected void sendSingleRequest(final String REQUEST_URL, final String QUERY_PARAM_NAME, final String QUERY_PARAM_VALUE) {
        final ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
        final WebTarget webTarget = resteasyClient.target(REQUEST_URL).queryParam(QUERY_PARAM_NAME, QUERY_PARAM_VALUE);
        final Response response = webTarget.request().get();
        log.debug("Response from server was:{}", response.getStatus());
        response.close();
        resteasyClient.close();
    }
}
