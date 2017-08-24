# Wildfly-Swarm JMS 2.0 Remote Broker Example


## Background

All examples available are very simple and assume local broker (your application and activemq broker is same wildfly-swarm instance). More often then not, due to various reasons
this will not be the case, and one will have wildfly-swarm application and activemq broker in different jvms (in this case activemq broker will be one wildfly-swarm instance and application/message producer will be another wildfly-swarm instance).
This example shows how to set up Wildfly-Swarm instance with AMQ Broker accepting remote connections, and another Wildfly-Swarm instance/simple app which will post message to the broker. Security is off, as this is example, and dealing with security is another layer of "goodness".

## Requirements

1. Java 8

## Running

Update src/main/env/devel/service-config.yml in both maven modules with your environment details.
Minimally in message-sender's service-config.yml set host to IP address on which the broker is running.

Build with `mvn clean  install` and start both broker and sender (assuming here running them on same machine, with port offset)

Broker:
```
mvn clean install
java -jar -Dswarm.project.stage.file=src/main/env/devel/service-config.yml target/message-broker-1.0.1-SNAPSHOT-hollow-swarm.jar target/message-broker-1.0.1-SNAPSHOT.war
```

Sender:
```
mvn clean install
java -Dswarm.port.offset=100 -Dswarm.project.stage.file=src/main/env/devel/service-config.yml -jar target/message-sender-1.0.1-SNAPSHOT-hollow-swarm.jar target/message-sender-1.0.1-SNAPSHOT.war
```

### Testing:

To post message do:

```
curl http://localhost:8180/sender
```

To check that it worker (see value in JSON returned):
```
curl http://localhost:8080/jolokia/read/org.apache.activemq.artemis:module\=JMS,name\=\"clustered-mediation-queue\",type=Queue/MessageCount
```
