
package org.dekstroza.websockets.partitioning.impl.cow;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.COPY_ON_WRITE_LIST;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.enterprise.inject.Produces;

import org.dekstroza.websockets.partitioning.api.Partition;
import org.dekstroza.websockets.partitioning.api.PartitionType;

/**
 * Producer for copy on write partition implementation
 */
public class CowPartitionProducer {

    private static final int DEFAULT_MAX_PART = 10;
    private static final int DEFAULT_MAX_SESSIONS_PER_PARTITION = 100;

    /**
     * Producer method for partition collection
     *
     * @return Returns collection of partitions using copy on write list
     */
    @PartitionType(implementation = COPY_ON_WRITE_LIST)
    @Produces
    public Collection<Partition> createPartitionCollection() {
        final CopyOnWriteArrayList<Partition> collection = new CopyOnWriteArrayList<>();
        for (int i = 0; i < DEFAULT_MAX_PART; i++) {
            final String partitionName = "Partition-" + i;
            collection.add(new CoWPartitionImpl(DEFAULT_MAX_SESSIONS_PER_PARTITION, partitionName));
        }
        return collection;
    }
}
