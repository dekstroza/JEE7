package org.dekstroza.websockets.partitioning.impl.clq;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.CONCURENT_LINKED_QUEUE;

import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.dekstroza.websockets.partitioning.api.AbstractPartitionManager;
import org.dekstroza.websockets.partitioning.api.Partition;
import org.dekstroza.websockets.partitioning.api.PartitionManagerType;
import org.dekstroza.websockets.partitioning.api.PartitionType;

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
