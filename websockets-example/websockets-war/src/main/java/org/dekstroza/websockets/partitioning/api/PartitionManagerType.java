package org.dekstroza.websockets.partitioning.api;

import static org.dekstroza.websockets.partitioning.api.Partition.Implementations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface PartitionManagerType {
    Implementations implementation();
}
