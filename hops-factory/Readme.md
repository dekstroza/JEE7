# Wildfly Swarm microservices

Simple implementation of three services with wildfly-swarm.

To create database, you neeed postgres and run first time with:
```
mvn clean install -DcreateDB
```

All postgres data is configurable in main pom.xml. Defaults for inital connection to postgres are:
```
        <db.name>hops_factory</db.name>
        <db.host.dev>localhost</db.host.dev>
        <db.port.dev>5432</db.port.dev>
        <db.root.name>postgres</db.root.name>
        <db.username.dev>postgres</db.username.dev>
        <db.password.dev>postgres</db.password.dev>
```

After running with -DcreateDB that usual mvn clean install will do.

Docker containers are created by specifying -Ddocker on maven command line.
All services will use same db, called hops_factory by default, with different schemas and users.
