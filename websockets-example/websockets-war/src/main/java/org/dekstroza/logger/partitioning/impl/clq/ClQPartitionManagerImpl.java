package org.dekstroza.logger.partitioning.impl.clq;

import static org.dekstroza.logger.partitioning.api.Partition.Implementations.CONCURENT_LINKED_QUEUE;

import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.dekstroza.logger.partitioning.api.AbstractPartitionManager;
import org.dekstroza.logger.partitioning.api.Partition;
import org.dekstroza.logger.partitioning.api.PartitionManagerType;
import org.dekstroza.logger.partitioning.api.PartitionType;

@PartitionManagerType(implementation = CONCURENT_LINKED_QUEUE)
public class ClQPartitionManagerImpl extends AbstractPartitionManager {

    @PartitionType(implementation = CONCURENT_LINKED_QUEUE)
    @Inject
    private Collection<Partition> partitions;

    @Override
    public Iterator<Partition> getPartitions() {
        return partitions.iterator();
    }

}
