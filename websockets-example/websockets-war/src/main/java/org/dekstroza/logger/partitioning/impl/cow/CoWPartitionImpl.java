package org.dekstroza.logger.partitioning.impl.cow;

import static org.dekstroza.logger.partitioning.api.Partition.Implementations.COPY_ON_WRITE_LIST;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.Session;

import org.dekstroza.logger.partitioning.api.BasePartition;
import org.dekstroza.logger.partitioning.api.PartitionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CopyOnWrite list implementation of partitioning
 */
@PartitionType(implementation = COPY_ON_WRITE_LIST)
public class CoWPartitionImpl extends BasePartition {

    private Logger log = LoggerFactory.getLogger(CoWPartitionImpl.class);

    private final String name;
    private final int maxSessionNumber;
    private final CopyOnWriteArrayList<Session> sessions;

    /**
     * Default constructor, will create partitioning holding 100 sessions and UUID generated name
     */
    public CoWPartitionImpl() {
        this(100, UUID.randomUUID().toString());
    }

    /**
     * Constructor taking max number of sessions and name as it's argument
     *
     * @param maxSessionNumber
     *            Maximum number of sessions allowed in this group
     * @param name
     *            Name of the partitioning
     */
    public CoWPartitionImpl(final int maxSessionNumber, final String name) {
        this.maxSessionNumber = maxSessionNumber;
        this.name = name;
        this.sessions = new CopyOnWriteArrayList<>();
    }

    /**
     * Add new session to this partitioning, session can not be null
     *
     * @param session
     *            Session to be added
     */
    public void addSession(final Session session) {
        log.debug("Adding session {}", session.getId());
        if (this.sessions.size() < maxSessionNumber) {
            sessions.add(session);
        } else {
            throw new IllegalStateException("partitioning is full.");
        }
    }

    /**
     * Remove session from the session list
     *
     * @param session
     *            Session to remove.
     */
    public boolean removeSession(final Session session) {
        return this.sessions.remove(session);
    }

    /**
     * Returns name of this partitioning
     *
     * @return Name of the partitioning
     */
    public String getName() {
        return name;
    }

    /**
     * Send message to all sessions in this partitioning
     *
     * @param message
     *            Message to send, can not be null
     */
    public void broadcastMessage(final String message) {
        for (Session session : this.sessions) {
            sendMessage(session, message);
        }
    }

    /**
     * Is the partitioning empty
     *
     * @return true if this partitioning is empty, false if it holds at least one session
     */
    public boolean isEmpty() {
        return this.sessions.isEmpty();
    }

    /**
     * Return number of sessions in this partitioning
     *
     * @return Number of sessions in this partitioning
     */
    public int getPartitionSize() {
        return this.sessions.size();
    }
}
