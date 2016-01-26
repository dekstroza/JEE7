package org.dekstroza.websockets.partitioning.impl.synchronizedlist;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.SYNCHRONIZED_LIST;

import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.dekstroza.websockets.partitioning.api.AbstractPartitionManager;
import org.dekstroza.websockets.partitioning.api.Partition;
import org.dekstroza.websockets.partitioning.api.PartitionManagerType;
import org.dekstroza.websockets.partitioning.api.PartitionType;
import org.slf4j.Logger;

/**
 * Producer for synchronized partition implementation
 */
@PartitionManagerType(implementation = SYNCHRONIZED_LIST)
public class SynchronizedListPartitionManagerImpl extends AbstractPartitionManager {

    @PartitionType(implementation = SYNCHRONIZED_LIST)
    @Inject
    private Collection<Partition> partitions;

    @Inject
    private Logger log;

    /**
     * Returns iterator for the partitions.
     *
     * @return Iterator<Partition>
     */
    public Iterator<Partition> getPartitions() {
        return this.partitions.iterator();
    }
}
