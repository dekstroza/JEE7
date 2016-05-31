package io.dekstroza.github.jee7.swarmdemo.app.services.impl;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

import javax.ejb.*;

@Singleton
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Startup
public class StatsdCommunicator {

    private static final StatsDClient statsd = new NonBlockingStatsDClient("ExampleApp", "localhost", 8125);

    @Asynchronous
    public void recordLatency(final long latency) {
        statsd.recordGaugeValue("Latency", latency);
    }
}
