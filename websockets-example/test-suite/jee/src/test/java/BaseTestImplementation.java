import java.net.URI;
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

public class BaseTestImplementation {

    private static final Logger log = LoggerFactory.getLogger(BaseTestImplementation.class);

    /**
     * <pre>
     * Time to wait for websockets to be created - confirmed to be open,
     * and time to wait for web socket message received confirmation.
     * </pre>
     **/
    private static final int TEST_TIMEOUT = 60;
    /**
     * Timeout units for above waits.
     */
    private static final TimeUnit TEST_TIMEOUT_UNIT = TimeUnit.SECONDS;
    /**
     * Set number of web socket clients connected to our server.
     */
    private static final int NUM_OF_SOCK_CLIENTS = 650;

    /**
     * Number of thread for executor service, when creating web sockets.
     */
    private static final int MAX_THREAD = 4;

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

    protected void singleRequestToMultipleClients(final String WEBSOCKET_URI, final String REQUEST_URL, final String QUERY_PARAM_NAME,
                                                  final String QUERY_PARAM_VALUE) throws Exception {

        final WebSocketFactory webSocketFactory = new WebSocketFactory(new URI(WEBSOCKET_URI), NUM_OF_SOCK_CLIENTS, TEST_TIMEOUT, TEST_TIMEOUT_UNIT);
        Assert.assertTrue(createWebSockets(webSocketFactory).get(TEST_TIMEOUT, TEST_TIMEOUT_UNIT));
        log.info("Created {} websocket(s). Invoking rest interface with single log message.", NUM_OF_SOCK_CLIENTS);
        sendSingleRequest(REQUEST_URL, QUERY_PARAM_NAME, QUERY_PARAM_VALUE);
        log.info("Waiting for all websockets to confirm message delivery.");
        final long start = System.nanoTime();
        final boolean isConfirmed = webSocketFactory.msgReceivedByAllWebSockets();
        final long end = System.nanoTime();
        Assert.assertTrue(isConfirmed);
        log.info("Time taken to deliver single message to {} client(s) was:[{} nanoseconds].", NUM_OF_SOCK_CLIENTS, (end - start));
        webSocketFactory.closeAllSockets();
        log.info("Closed all sockets...");
    }

    protected Future<Boolean> createWebSockets(final WebSocketFactory factory) {
        final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD);
        return executorService.submit(factory);
    }

    protected void sendSingleRequest(final String REQUEST_URL, final String QUERY_PARAM_NAME, final String QUERY_PARAM_VALUE) {
        final ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
        final WebTarget webTarget = resteasyClient.target(REQUEST_URL).queryParam(QUERY_PARAM_NAME, QUERY_PARAM_VALUE);
        final Response response = webTarget.request().get();
        log.info("Response from server was:{}", response.getStatus());
        response.close();
        resteasyClient.close();
    }
}
