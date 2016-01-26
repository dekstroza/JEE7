package confenv;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.dekstroza.logger.configuration.api.ConfigurationEnvironment;
import org.dekstroza.logger.partitioning.api.PartitionManager;
import org.slf4j.Logger;

public class SimpleConfigurationEnvironment implements ConfigurationEnvironment {

    @Inject
    private Logger log;

    @Any
    @Inject
    private PartitionManager partitionManager;

    public PartitionManager selectPartitionManagerImplementation() {
        log.info("Selected partition manager implementation:{}", partitionManager.getClass().getCanonicalName());
        return partitionManager;
    }
}
