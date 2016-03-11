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

package main;

import org.dekstroza.swarm.application.PaymentsApplication;
import org.dekstroza.swarm.application.utils.logging.LoggerProducer;
import org.dekstroza.swarm.payments.api.Payment;
import org.dekstroza.swarm.payments.endpoint.PaymentsRestEndpoint;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

public class Main {

    public static void main(String[] args) throws Exception {
        final Container container = new Container();
        container.fraction(new DatasourcesFraction().jdbcDriver("h2", (d) -> {
            d.driverClassName("org.h2.Driver");
            d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
            d.driverModuleName("com.h2database.h2");
        }).dataSource("ExampleDS", (ds) -> {
            ds.driverName("h2");
            ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            ds.userName("sa");
            ds.password("sa");
        }));

        container.start();
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "application-war.war");
        jaxrsArchive.addResource(PaymentsApplication.class);
        jaxrsArchive.addClass(PaymentsRestEndpoint.class);
        jaxrsArchive.addClass(LoggerProducer.class);
        jaxrsArchive.addClass(Payment.class);
        jaxrsArchive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        jaxrsArchive.addAllDependencies();
        container.deploy(jaxrsArchive);

    }
}
