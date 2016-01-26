package org.dekstroza.websockets.partitioning.impl.clq;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.CONCURENT_LINKED_QUEUE;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.Session;

import org.dekstroza.websockets.partitioning.api.BasePartition;
import org.dekstroza.websockets.partitioning.api.PartitionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PartitionType(implementation = CONCURENT_LINKED_QUEUE)
public class ClQPartitionImpl extends BasePartition {

    private Logger log = LoggerFactory.getLogger(ClQPartitionImpl.class);

    private final String partitionName;
    private final int maxSessionNumber;
    private final Queue<Session> sessions;

    /**
     * Default constructor
     */
    public ClQPartitionImpl() {
        this(100, UUID.randomUUID().toString());
    }

    /**
     * Full argument constructor
     *
     * @param maxSessionNumber
     *            Maximum number of sessions in this partitioning.
     * @param partitionName
     *            Name of the partitioning.
     */
    public ClQPartitionImpl(final int maxSessionNumber, final String partitionName) {
        this.partitionName = partitionName;
        this.maxSessionNumber = maxSessionNumber;
        this.sessions = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addSession(final Session session) {
        log.debug("Adding session {}", session.getId());
        if (this.sessions.size() < maxSessionNumber) {
            sessions.add(session);
        } else {
            throw new IllegalStateException("partitioning is full.");
        }
    }

    @Override
    public boolean removeSession(final Session session) {
        return sessions.remove(session);
    }

    @Override
    public String getName() {
        return partitionName;
    }

    @Override
    public void broadcastMessage(final String message) {
        for (final Session session : sessions) {
            sendMessage(session, message);
        }

    }

    @Override
    public boolean isEmpty() {
        return sessions.isEmpty();
    }

    @Override
    public int getPartitionSize() {
        return sessions.size();
    }
}
