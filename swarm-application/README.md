# Swarm and JEE7 Example II

## About

Example of JEE7 code using JAXRS with WELD, packaged and deployed in docker container as wildfly-swarm.
Database backend is PostgreSQL 9.x, with predifiend schema runing in docker container.

## Running the code

1. Build and deploy database container

```
$ cd database-container && docker build -t db_backend . 
$ docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d db_backend
```
Note the db ip address and update Main class in payment-backend, then build:
```
$ mvn clean install //or
$ mvn clean install -Pdocker //to build docker container as well
```

To run the code:
```
$ java -jar payment-backend/target/payment-backend-1.0.1-SNAPSHOT-swarm.jar
```

Or run the container with usual docker run ....

Happy hacking,
Dejan