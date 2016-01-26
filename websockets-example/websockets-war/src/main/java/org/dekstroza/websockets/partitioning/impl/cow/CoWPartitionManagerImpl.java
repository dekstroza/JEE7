
package org.dekstroza.websockets.partitioning.impl.cow;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.COPY_ON_WRITE_LIST;

import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.dekstroza.websockets.partitioning.api.AbstractPartitionManager;
import org.dekstroza.websockets.partitioning.api.Partition;
import org.dekstroza.websockets.partitioning.api.PartitionManagerType;
import org.dekstroza.websockets.partitioning.api.PartitionType;

/**
 * Encapsulates partitions and sessions, hiding partitioning from calling services.
 */
@PartitionManagerType(implementation = COPY_ON_WRITE_LIST)
public class CoWPartitionManagerImpl extends AbstractPartitionManager {

    @PartitionType(implementation = COPY_ON_WRITE_LIST)
    @Inject
    private Collection<Partition> partitions;

    /**
     * Returns iterator for the partitions.
     *
     * @return Iterator<Partition>
     */
    public Iterator<Partition> getPartitions() {
        return this.partitions.iterator();
    }

}
