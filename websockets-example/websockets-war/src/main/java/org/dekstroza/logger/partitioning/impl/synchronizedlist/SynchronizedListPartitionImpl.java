package org.dekstroza.logger.partitioning.impl.synchronizedlist;

import static org.dekstroza.logger.partitioning.api.Partition.Implementations.SYNCHRONIZED_LIST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import javax.websocket.Session;

import org.dekstroza.logger.partitioning.api.BasePartition;
import org.dekstroza.logger.partitioning.api.PartitionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PartitionType(implementation = SYNCHRONIZED_LIST)
public class SynchronizedListPartitionImpl extends BasePartition {

    private Logger log = LoggerFactory.getLogger(SynchronizedListPartitionImpl.class);

    private final String partitionName;
    private final int maxSessionNumber;
    private final Collection<Session> sessions;

    /**
     * Default constructor
     */
    public SynchronizedListPartitionImpl() {
        this(100, UUID.randomUUID().toString());
    }

    /**
     * Full arg constructor, taking max session number for the partitioning and name as arguments
     * 
     * @param maxSessionNumber
     *            Maximum number of sessions this partitioning can hold
     * @param name
     *            Name of this partitioning
     */
    public SynchronizedListPartitionImpl(final int maxSessionNumber, final String name) {
        this.partitionName = name;
        this.maxSessionNumber = maxSessionNumber;
        this.sessions = Collections.synchronizedList(new ArrayList<Session>());
    }

    @Override
    public void addSession(Session session) {

        if (!sessions.add(session))
            throw new IllegalStateException("Unable to add session into partitioning:" + partitionName);
    }

    @Override
    public boolean removeSession(Session session) {
        return this.sessions.remove(session);
    }

    @Override
    public String getName() {
        return this.partitionName;
    }

    @Override
    public void broadcastMessage(String message) {

        for (final Session session : sessions) {
            sendMessage(session, message);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.sessions.isEmpty();
    }

    @Override
    public int getPartitionSize() {
        return this.sessions.size();
    }
}
