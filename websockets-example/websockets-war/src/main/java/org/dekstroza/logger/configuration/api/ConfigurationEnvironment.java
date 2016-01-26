package org.dekstroza.logger.configuration.api;

import org.dekstroza.logger.partitioning.api.PartitionManager;

public interface ConfigurationEnvironment {

    PartitionManager selectPartitionManagerImplementation();
}
