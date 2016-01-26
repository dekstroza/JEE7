
package org.dekstroza.websockets.partitioning.impl.synchronizedlist;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.SYNCHRONIZED_LIST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.enterprise.inject.Produces;

import org.dekstroza.websockets.partitioning.api.Partition;
import org.dekstroza.websockets.partitioning.api.PartitionType;

/**
 * Producer for synchronized partition implementation
 */
public class SynchronizedListPartitionProducer {

    private static final int DEFAULT_MAX_PART = 10;
    private static final int DEFAULT_MAX_SESSIONS_PER_PARTITION = 100;

    /**
     * Producer method for partition collection
     * 
     * @return Returns collection of partitions using synchronized array list
     */
    @PartitionType(implementation = SYNCHRONIZED_LIST)
    @Produces
    public Collection<Partition> createPartitionCollection() {

        final Collection<Partition> partitions = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < DEFAULT_MAX_PART; i++) {
            final String partitionName = "Partition-" + i;
            partitions.add(new SynchronizedListPartitionImpl(DEFAULT_MAX_SESSIONS_PER_PARTITION, partitionName));
        }
        return partitions;
    }
}
