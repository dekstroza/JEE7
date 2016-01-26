package org.dekstroza.websockets.configuration.api;

import org.dekstroza.websockets.partitioning.api.PartitionManager;

public interface ConfigurationEnvironment {

    PartitionManager selectPartitionManagerImplementation();
}
