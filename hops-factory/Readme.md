# Wildfly Swarm microservices

Simple implementation of three services with wildfly-swarm. Examples feature cdi, jpa2, jaxrs, flyway and swagger fractions.

## Database settings
To create database, you neeed postgres, and initial database setup. To create it, on first run do:
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

After running ```mvn clean install -DcreateDB```, usual ```mvn clean install``` will do just fine.
To clean everything, run ```mvn clean install -DcleanDB```, which will completely delete database and all associated roles with these three services.

## Docker
Docker containers are created by specifying ```mvn clean install -Ddocker``` on command line.
All services will use same db, called hops_factory by default, with different schemas and users.

All docker containers are built using hollow jar feature of swarm, and expect service configuration in /etc/config/service-config.yml, so mount it there when running docker containers. Each service project has src/main/env/devel which is filtered and used when running service for testing - those can be mounted in production as well.

Example of service-config.yml:

```
swarm:
  io:
    workers:
      default:
        io-threads: 4
        task-max-threads: 32
  undertow:
    servers:
      default-server:
        http-listeners:
          default:
            enable-http2: true
  http2:
    enabled: true
  flyway:
    jdbc-user: postgres
    jdbc-password: postgres
    jdbc-url: jdbc:postgresql://localhost:5432/hops_factory?currentSchema=customerserviceflyway
  datasources:
    data-sources:
      CustomerDS:
        connection-url: jdbc:postgresql://localhost:5432/hops_factory
        driver-name: postgresql
        user-name: customer_service_app
        password: 123hopS
        
```
So run command would look like:

```
docker run -d -v service-config.yml:/etc/config/service-config.yml -p 8080:8080 customer_service:1.0.1-SNAPSHOT
```

## Healthchecks

Each service has basic healthcheck exposed at http://localhost:8080/${service.name}/healthz which will return 200 if everything is ok.

## Loging

Each service has runtime adjustable logger, you can see available loggers at: 
http://localhost/${service.name}/admin/logger , 
and enable different log levels with for example: 
http://localhost:8080/${service.name}/admin/logger/com.something.blabla?level=DEBUG

## Swagger

Example swagger doc is available for supplier service, it is accessible at:
http://localhost:8080/supplier_service/swagger.json
