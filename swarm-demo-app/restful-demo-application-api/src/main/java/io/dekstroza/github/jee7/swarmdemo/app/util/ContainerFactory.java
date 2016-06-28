package io.dekstroza.github.jee7.swarmdemo.app.util;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jpa.JPAFraction;

public class ContainerFactory {

    public static final Container create() throws Exception {
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
            ds.initialPoolSize(20);
            ds.minPoolSize(20);
            ds.maxPoolSize(20);
        }));

        // Prevent JPA Fraction from installing it's default datasource fraction
        container.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource("jboss/datasources/MyDS"));
        return container;
    }
}
