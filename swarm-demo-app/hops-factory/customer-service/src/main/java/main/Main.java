package main;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.flyway.FlywayFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;

public class Main {

    public static final String DATABASE_CONNECTION_URL = "database.connection.url";
    public static final String DATABASE_CONNECTION_USERNAME = "database.connection.username";
    public static final String DATABASE_CONNECTION_PASSWORD = "database.connection.password";
    public static final String DB_SCHEMA = "customer_service";
    public static final String DS_NAME = "CustomerDS";
    public static final String DATA_SOURCE = "jboss/datasources/" + DS_NAME;

    public static void main(String[] args) throws Exception {

        final Swarm swarm = new Swarm(new Properties());
        final String jdbcURL = swarm.configView().resolve(DATABASE_CONNECTION_URL).getValue();
        final String dbUsername = swarm.configView().resolve(DATABASE_CONNECTION_USERNAME).getValue();
        final String dbPassword = swarm.configView().resolve(DATABASE_CONNECTION_PASSWORD).getValue();

        swarm.fraction(new DatasourcesFraction().jdbcDriver("postgresql", (driver) -> {
            driver.driverClassName("org.postgresql.Driver");
            driver.driverModuleName("org.postgresql");
        }).dataSource(DS_NAME, (dataSource) -> {
            dataSource.driverName("postgresql");
            dataSource.connectionUrl(jdbcURL);
            dataSource.userName(dbUsername);
            dataSource.password(dbPassword);
        })).fraction(new JPAFraction().defaultDatasource(DATA_SOURCE))
                .fraction(new FlywayFraction().jdbcPassword(dbPassword).jdbcUrl(jdbcURL + "?currentSchema=" + DB_SCHEMA).jdbcUser(dbUsername));

        swarm.start();
        swarm.deploy(createDeployment());

    }

    public static JAXRSArchive createDeployment() throws Exception {
        WebArchive warArchive = ShrinkWrap.create(WebArchive.class, "customer-service.war");
        warArchive.addPackages(true, "com.github.dekstroza.hopsfactory");
        Files.walk(Paths.get("src/main/resources/db/migration")).filter(Files::isRegularFile).forEach(path -> {
            try {
                warArchive.addAsWebInfResource(path.toUri().toURL(), "classes/db/migration/" + path.getFileName());
            } catch (MalformedURLException me) {
                me.printStackTrace();
            }
        });

        warArchive.addAsWebInfResource(new ClassLoaderAsset("persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        //warArchive.addAsWebInfResource(new File("src/main/resources/db/migration/*"), "classes/db/migration");
        return warArchive.as(JAXRSArchive.class).addAllDependencies();
    }
}
