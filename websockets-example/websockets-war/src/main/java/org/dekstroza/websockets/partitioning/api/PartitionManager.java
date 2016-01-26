package org.dekstroza.websockets.partitioning.api;

import java.util.Iterator;

import javax.websocket.Session;

/**
 * Partition manager interface, implemented by different Implementations of session partitioning
 */
public interface PartitionManager {

    static enum Implementations {
        COPY_ON_WRITE_LIST, CONCURENT_LINKED_QUEUE, SYNCHRONIZED_LIST
    }

    /**
     * Add new session. New session will be added into first available session group
     *
     * @param session
     *            Session to add.
     */
    void addSession(final Session session);

    /**
     * Remove session by iterating through session groups
     *
     * @param session
     *            Session to remove.
     */
    void removeSession(final Session session);

    /**
     * Return number of sessions across all partitions
     *
     * @return Number of sessions
     */
    int getSessionCount();

    /**
     * Returns iterator for the session groups
     *
     * @return Iterator<SessionGroup>
     */
    Iterator<Partition> getPartitions();

}
