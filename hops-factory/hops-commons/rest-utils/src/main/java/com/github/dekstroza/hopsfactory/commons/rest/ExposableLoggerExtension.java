package com.github.dekstroza.hopsfactory.commons.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class ExposableLoggerExtension implements Extension {

    private static final Logger log = LoggerFactory.getLogger(ExposableLoggerExtension.class);
    private static final List<String> endpointsWithLogControl = new ArrayList<>();

    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        AnnotatedType<T> at = pat.getAnnotatedType();

        if (at.isAnnotationPresent(ExposeLogControl.class)) {
            log.info("Exposing log control on {}", at.getJavaClass().getCanonicalName());
            endpointsWithLogControl.add(at.getJavaClass().getCanonicalName());
        }

    }

    /**
     * Return unmodifiable list of all detected endpoints with log control
     *
     * @return unmodifiable list of FQCN with log control annotation
     */
    public static List<String> getEndpointsWithLogControl() {
        return Collections.unmodifiableList(endpointsWithLogControl);
    }
}
