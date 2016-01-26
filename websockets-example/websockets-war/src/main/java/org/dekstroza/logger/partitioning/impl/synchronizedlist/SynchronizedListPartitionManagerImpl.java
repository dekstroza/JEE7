package org.dekstroza.logger.partitioning.impl.synchronizedlist;

import static org.dekstroza.logger.partitioning.api.Partition.Implementations.SYNCHRONIZED_LIST;

import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.dekstroza.logger.partitioning.api.AbstractPartitionManager;
import org.dekstroza.logger.partitioning.api.Partition;
import org.dekstroza.logger.partitioning.api.PartitionManagerType;
import org.dekstroza.logger.partitioning.api.PartitionType;
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
