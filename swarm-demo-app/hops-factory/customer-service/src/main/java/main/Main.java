package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.flyway.FlywayFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;

public class Main {

    public static void main(String[] args) throws Exception {
        final Swarm swarm = new Swarm();

        swarm.fraction(new DatasourcesFraction().jdbcDriver("postgresql", (driver) -> {
            driver.driverClassName("org.postgresql.Driver");
            driver.driverModuleName("org.postgresql");
        }).dataSource("CustomerDS", (dataSource) -> {
            dataSource.driverName("postgresql");
            dataSource.connectionUrl("jdbc:postgresql://localhost:5432/hops-factory");
            dataSource.userName("customer_service_app");
            dataSource.password("123hopS");
        }));
        swarm.fraction(new JPAFraction().defaultDatasource("jboss/datasources/CustomerDS"));
        FlywayFraction flywayFraction = new FlywayFraction().jdbcPassword("123hopS").jdbcUrl("jdbc:postgresql://localhost:5432/hops-factory?currentSchema=customer_service")
                .jdbcUser("customer_service_app");
        swarm.fraction(flywayFraction);
        swarm.start();
        swarm.deploy(createDeployment());

    }

    public static JAXRSArchive createDeployment() throws Exception {
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "customer-service.war");
        jaxrsArchive.addPackages(true, "com.github.dekstroza.hopsfactory");
        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        jaxrsArchive.addAllDependencies();
        return jaxrsArchive;
    }
}
