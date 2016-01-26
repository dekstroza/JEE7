package org.dekstroza.websockets.partitioning.api;

import javax.websocket.Session;

/**
 * Partition interface
 */
public interface Partition {

    enum Implementations {
        COPY_ON_WRITE_LIST, SYNCHRONIZED_LIST, CONCURENT_LINKED_QUEUE
    }

    /**
     * Add new session to this partitioning, session can not be null
     *
     * @param session
     *            Session to be added
     */
    void addSession(final Session session);

    /**
     * Remove session from the session list
     *
     * @param session
     *            Session to remove.
     */
    boolean removeSession(final Session session);

    /**
     * Returns name of this partitioning
     *
     * @return Name of the partitioning
     */
    String getName();

    /**
     * Send message to all sessions in this partitioning
     *
     * @param message
     *            Message to send, can not be null
     */
    void broadcastMessage(final String message);

    /**
     * Is the partitioning empty
     *
     * @return true if this partitioning is empty, false if it holds at least one session
     */
    boolean isEmpty();

    /**
     * Return number of sessions in this partitioning
     *
     * @return Number of sessions in this partitioning
     */
    int getPartitionSize();
}
