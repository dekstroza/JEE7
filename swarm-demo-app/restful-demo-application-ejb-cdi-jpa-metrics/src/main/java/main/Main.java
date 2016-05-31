package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;

import io.dekstroza.github.jee7.swarmdemo.app.RestApplication;

public class Main {

    public static void main(String[] args) throws Exception {
        final Container container = new Container();

        container.fraction(new DatasourcesFraction().jdbcDriver("h2", (d) -> {
            d.driverClassName("org.h2.Driver");
            d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
            d.driverModuleName("com.h2database.h2");
        }).dataSource("MyDS", (ds) -> {
            ds.driverName("h2");
            ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            ds.userName("sa");
            ds.password("sa");
        }));

        // Prevent JPA Fraction from installing it's default datasource fraction
        container.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource("jboss/datasources/MyDS"));

        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "restful-demo-app-ejb-cdi-jpa.war");
        jaxrsArchive.addPackages(true, "io.dekstroza.github.jee7.swarmdemo");
        jaxrsArchive.addResource(RestApplication.class);

        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()),
                "classes/META-INF/persistence.xml");
        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();

        //jaxrsArchive.as(TopologyArchive.class).advertise();
        container.start();
        container.deploy(jaxrsArchive);

    }
}
