package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;

public class Main {

    public static void main(String[] args) throws Exception {
        final Swarm swarm = createSwarm();
        swarm.start();
        swarm.deploy(createDeployment());
    }

    public static JAXRSArchive createDeployment() throws Exception {
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "registration-service.war");
        jaxrsArchive.addPackages(true, "io.dekstroza.github.jee7.swarmdemo.app");
        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        jaxrsArchive.addAllDependencies();
        return jaxrsArchive;
    }

    public static Swarm createSwarm() throws Exception {
        final Swarm swarm = new Swarm();
        swarm.fraction(new DatasourcesFraction().jdbcDriver("h2", (driver) -> {
            driver.driverClassName("org.h2.Driver");
            driver.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
            driver.driverModuleName("com.h2database.h2");
        }).dataSource("RegistrationsDS", (dataSource) -> {
            dataSource.driverName("h2");
            dataSource.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            dataSource.userName("sa");
            dataSource.password("sa");
        }));
        swarm.fraction(new JPAFraction().defaultDatasource("jboss/datasources/RegistrationsDS"));
        return swarm;
    }
}
