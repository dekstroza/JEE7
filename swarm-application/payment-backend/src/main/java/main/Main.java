package main;

import org.dekstroza.swarm.application.PaymentsApplication;
import org.dekstroza.swarm.application.utils.logging.LoggerProducer;
import org.dekstroza.swarm.payments.api.Payment;
import org.dekstroza.swarm.payments.api.PaymentInsertResponse;
import org.dekstroza.swarm.payments.endpoint.PaymentsRestEndpoint;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * Main entry point for swarm
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final Container container = new Container();

        container.fraction(new DatasourcesFraction().jdbcDriver("org.postgresql", (d) -> {
            d.driverClassName("org.postgresql.Driver");
            d.xaDatasourceClass("org.postgresql.xa.PGXADataSource");
            d.driverModuleName("org.postgresql");
        }).dataSource("ExampleDS", (ds) -> {
            ds.driverName("org.postgresql");
            ds.connectionUrl("jdbc:postgresql://172.18.0.2:5432/swarmapp");
            ds.userName("postgres");
            ds.password("mysecretpassword");
        }));

        container.start();
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "payments-backend.war");
        jaxrsArchive.addResource(PaymentsApplication.class);
        jaxrsArchive.addClass(PaymentsRestEndpoint.class);
        jaxrsArchive.addClass(LoggerProducer.class);
        jaxrsArchive.addClass(Payment.class);
        jaxrsArchive.addClass(PaymentInsertResponse.class);
        jaxrsArchive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        jaxrsArchive.addAllDependencies();
        container.deploy(jaxrsArchive);

    }
}
