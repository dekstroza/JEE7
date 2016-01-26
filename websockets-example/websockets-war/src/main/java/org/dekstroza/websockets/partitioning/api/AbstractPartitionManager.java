package org.dekstroza.websockets.partitioning.api;

import java.util.Iterator;

import javax.inject.Inject;
import javax.websocket.Session;

import org.slf4j.Logger;

public abstract class AbstractPartitionManager implements PartitionManager {

    @Inject
    private Logger log;

    /**
     * Iterator for collection of partitions
     * 
     * @return Iterator for partitions collection
     */
    public abstract Iterator<Partition> getPartitions();

    /**
     * Add session to one of the partitions
     * 
     * @param session
     *            Session to be added.
     */
    public void addSession(final Session session) {

        final Iterator<Partition> partitionIterator = getPartitions();
        while (partitionIterator.hasNext()) {
            final Partition partition = partitionIterator.next();
            try {
                partition.addSession(session);
                log.debug("Session {} has been added to {} session group", session.getId(), partition.getName());
                return;
            } catch (IllegalStateException ignored) {
            }
        }
        throw new IllegalStateException("Unable to add session, all session groups are full...");

    }

    /**
     * Remove session by iterating through session partitions
     *
     * @param session
     *            Session to remove.
     */
    public void removeSession(final Session session) {
        final Iterator<Partition> partitionIterator = getPartitions();
        while (partitionIterator.hasNext()) {
            final Partition partition = partitionIterator.next();
            if (partition.removeSession(session)) {
                log.debug("Session {} has been removed from session group {}.", session.getId(), partition.getName());
                return;
            }
        }
    }

    /**
     * Return number of sessions across all partitions
     *
     * @return Number of sessions
     */
    public int getSessionCount() {
        int sessionCount = 0;
        final Iterator<Partition> partitionIterator = getPartitions();
        while (partitionIterator.hasNext()) {
            final Partition partition = partitionIterator.next();
            sessionCount += partition.getPartitionSize();
        }
        return sessionCount;
    }
}
