package io.dekstroza.github.jee7.swarmdemo.app.services;

import java.util.UUID;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

public class StatsdCommunicator {

    private static StatsDClient statsd = null;
    private static StatsdCommunicator _instance = null;

    private StatsdCommunicator() {
        final String statsdHost = System.getProperty("statsdHost", "localhost");
        final int statsdPort = Integer.parseInt(System.getProperty("statsdPort", "8125"));
        final String nodeIdentifier = System.getProperty("swarm.node.id", UUID.randomUUID().toString());
        this.statsd = new NonBlockingStatsDClient(nodeIdentifier, statsdHost, statsdPort);
    }

    public static final StatsdCommunicator getInstance() {
        if (_instance == null) {
            _instance = new StatsdCommunicator();
        }
        return _instance;
    }

    public void recordLatency(final String gaugeName, final long latency) {
        statsd.recordGaugeValue(gaugeName, latency);
    }
}
