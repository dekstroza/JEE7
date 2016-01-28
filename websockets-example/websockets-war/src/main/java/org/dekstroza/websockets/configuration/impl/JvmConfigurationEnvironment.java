package org.dekstroza.websockets.configuration.impl;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations.COPY_ON_WRITE_LIST;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.dekstroza.websockets.configuration.api.ConfigurationEnvironment;
import org.dekstroza.websockets.partitioning.api.Partition;
import org.dekstroza.websockets.partitioning.api.PartitionManager;
import org.dekstroza.websockets.partitioning.api.PartitionManagerTypeQualifier;
import org.slf4j.Logger;

public class JvmConfigurationEnvironment implements ConfigurationEnvironment {

    @Any
    @Inject
    private Instance<PartitionManager> partitionManagers;

    @Inject
    private Logger log;

    @Override
    public PartitionManager selectPartitionManagerImplementation() {
        Partition.Implementations selectedType = COPY_ON_WRITE_LIST;
        try {
            selectedType = Partition.Implementations.valueOf(System.getProperty("pmImplementation"));
        } catch (Exception e) {
            log.warn("Uknown partition manager implementation, or implementation not specified. Using default implementation.");
        }
        final PartitionManagerTypeQualifier qualifier = new PartitionManagerTypeQualifier(selectedType);
        final PartitionManager partitionManager = partitionManagers.select(qualifier).get();
        log.info("Selected implementation of partition manager is:{}", partitionManager.getClass().getCanonicalName());
        return partitionManager;
    }

}
