## Websockets example

Combination of JSR 356 mixed with CDI, EJB, JAXRS and some arquillian.

Includes arquillian test(s) with connection to 1000 websocket clients, measuring time to deliver 1 message to all of them, using three different versions of session partitioning.
To compile and run:
```
mvn clean install
```
To run using wildfly swarm:
```
mvn clean install -Pswarm 
java [-DpmImplementation=[COPY_ON_WRITE_LIST |SYNCHRONIZED_LIST | CONCURENT_LINKED_QUEUE]] -jar websockets-war/target/websockets-war-1.0.1-SNAPSHOT-swarm.jar 
```
If -DpmImplementation is not specified on command line, it will run with default, which is: COPY_ON_WRITE_LIST.
To do something usefull with it, point the browser to http://localhost:8080/websockets-war/ and do a GET with log parameter, for example:
```
curl http://localhost:8080/websockets-war/websockets/insert?log="10:22:56,694%20INFO%20[org.jboss.weld.deployer]%20(MSC%20service%20thread%201-7)%20WFLYWELD0009:%20Starting%20weld%20service%20for%20deployment%20logger-war-1.0.1-SNAPSHOT.war"
```
