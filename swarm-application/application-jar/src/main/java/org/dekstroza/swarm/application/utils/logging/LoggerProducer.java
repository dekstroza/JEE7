/*
 * ------------------------------------------------------------------------------
 *  *******************************************************************************
 *  * COPYRIGHT Ericsson 2012
 *  *
 *  * The copyright to the computer program(s) herein is the property of
 *  * Ericsson Inc. The programs may be used and/or copied only with written
 *  * permission from Ericsson Inc. or in accordance with the terms and
 *  * conditions stipulated in the agreement/contract under which the
 *  * program(s) have been supplied.
 *  *******************************************************************************
 *  *----------------------------------------------------------------------------
 */

package org.dekstroza.swarm.application.utils.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer for slf4j logger
 */
public class LoggerProducer {

    /**
     * Produce logger for given injection point, using fully qualified class name to create logger
     * 
     * @param injectionPoint
     *            Injection point where logger is being created
     * @return slf4j logger
     */
    @Produces
    public Logger produceLogger(final InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
