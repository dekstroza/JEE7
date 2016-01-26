package org.dekstroza.websockets.partitioning.impl.clq;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.CONCURENT_LINKED_QUEUE;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.enterprise.inject.Produces;

import org.dekstroza.websockets.partitioning.api.Partition;
import org.dekstroza.websockets.partitioning.api.PartitionType;

/**
 * Producer for concurrent linked queue implementation.
 */
public class ClQPartitionProducer {

    private static final int DEFAULT_MAX_PART = 10;
    private static final int DEFAULT_MAX_SESSIONS_PER_PARTITION = 100;

    /**
     * Producer method for partition collection.
     *
     * @return Returns collection of partitions using concurent linked list.
     */
    @PartitionType(implementation = CONCURENT_LINKED_QUEUE)
    @Produces
    public Collection<Partition> createPartitionCollection() {
        final ConcurrentLinkedQueue<Partition> collection = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < DEFAULT_MAX_PART; i++) {
            final String partitionName = "Partition-" + i;
            collection.add(new ClQPartitionImpl(DEFAULT_MAX_SESSIONS_PER_PARTITION, partitionName));
        }
        return collection;
    }

}
