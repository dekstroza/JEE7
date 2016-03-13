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
        final String dbHost = System.getenv("dbHost") == null ? "localhost" : System.getenv("dbHost");
        final String dbPort = System.getenv("dbPort") == null ? "5432" : System.getenv("dbPort");
        final String dbName = System.getenv("dbName") == null ? "swarmapp" : System.getenv("dbName");
        final String dbUser = System.getenv("dbUser") == null ? "postgres" : System.getenv("dbUser");
        final String dbPassword = System.getenv("dbPassword") == null ? "mysecretpassword" : System.getenv("dbPassword");

        container.fraction(new DatasourcesFraction().jdbcDriver("org.postgresql", (d) -> {
            d.driverClassName("org.postgresql.Driver");
            d.xaDatasourceClass("org.postgresql.xa.PGXADataSource");
            d.driverModuleName("org.postgresql");
        }).dataSource("ExampleDS", (ds) -> {
            ds.driverName("org.postgresql");
            ds.connectionUrl(new StringBuilder("jdbc:postgresql://").append(dbHost).append(":").append(dbPort).append("/").append(dbName).toString());
            ds.userName(dbUser);
            ds.password(dbPassword);
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
