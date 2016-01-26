package org.dekstroza.logger.partitioning.api;

import static org.dekstroza.logger.partitioning.api.Partition.Implementations;

import javax.enterprise.util.AnnotationLiteral;

public class PartitionManagerTypeQualifier extends AnnotationLiteral<PartitionManagerType>implements PartitionManagerType {

    private Implementations impl;

    public PartitionManagerTypeQualifier(final Implementations impl) {
        this.impl = impl;
    }

    @Override
    public Implementations implementation() {
        return this.impl;
    }
}
