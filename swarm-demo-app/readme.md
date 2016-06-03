# Simple rest endpoint exposed using Wildfly Swarm #

### Requirements ###

1. Java 8
2. Maven (recent one)
3. Docker (recent installation with working docker-compose)

### Content ###
Demo application exposing few rest endpoints, some of which are secured with JWT for demonstration purposes. Application is packed and deployed using Wildfly Swarm (http://wildfly-swarm.io).
Project contains three maven modules, each building more on top of previous one, and repetition of code across all three of them is intentional.

Maven modules included are:

1. restful-demo-application
2. restful-demo-application-ejb-cdi-jpa
3. restful-demo-application-ejb-cdi-jpa-metrics
4. restful-demo-application-ejb-cdi-jpa-metrics-elastic

### restful-demo-application ###

Features basic example of rest point, secured and public using jaxrs & cdi. 
To build run ```mvn clean install```
To run simply type: 
```java -jar swarm-demo-app/restful-demo-application/target/restful-demo-application-1.0.1-SNAPSHOT-swarm.jar```

Exposed public endpoints are:

 1. Simple public endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/public/data```
 
 Will return Content-Type: text/plain and body: Some public data

 2. Login endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/login?username=dejan&password=kitic```
 
 Will return Content-Type: text/plain and header Authorization: "Bearer JWT", where JWT is valid json web token.
 Body will be empty.
 
Exposed secured enpoints are:

1. Simple secured endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/secure/data```
 
Will return 405 if Authorization header does not contain valid JWT, or will return Content-Type: text/plain and body: Some super secret data
 
### restful-demo-application-ejb-cdi-jpa ###

Features basic example of rest point plus jpa and ejb, exposing secured and public rest endpoints - same as previous example, building bit more on top of it. In this case, we add service topology and consul. When service is started it will attempt to register in consul catalog.
This example also demonstrates use of consul for service topology, so before running consul should be started. 
It is available in most linux distros and windows: https://www.consul.io

On OSX consul can be installed using brew, with: ```brew install consul``` and then we can run it with:

```consul agent -dev -bind IP_ADDRESS``` 

and it's ui will be also available at ```http://localhost:8500/ui/#/dc1/nodes```. Once consul agent is up, provide information about it when running jar:

To build run ```mvn clean install```

To run: 

```java -jar -Dswarm.consul.url="http://127.0.0.1:8500" swarm-demo-app/restful-demo-application-ejb-cdi-jpa/target/restful-demo-application-ejb-cdi-jpa-1.0.1-SNAPSHOT-swarm.jar```

Once running our app will register as a service in consul catalog, so check http://localhost:8500/ui/#/dc1/services/consul . Starting more instances of above swarm jar (add port offset with  -Dswarm.port.offset=N), will 
show more instances registered in the service catalog. This demontstrates integration with consul and ability to use consul from our app for service discovery (through api we can query for presence 
of other even non-JEE services, or even notify clients about availability of new service etc...)

Exposed public endpoints are:

 1. Simple public endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/public/data```
 
 Will return Content-Type: text/plain and body: Some public data
 2. Login endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/login?username=dejan&password=kitic```
 
 Will return Content-Type: text/plain and header Authorization: "Bearer JWT", where JWT is valid json web token.
 Body will be empty.
 
Exposed secured enpoints are:

1. Simple secured endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/secure/data```
 
Will return 405 if Authorization header does not contain valid JWT, or will return Content-Type: text/plain and body: Some super secret data
 
 
### restful-demo-application-ejb-cdi-jpa-metrics ### 

Features same as above, but not requiring consul anymore, and instead this one demonstrates use of grafana, statsd and influxdb for performance monitoring.
Before running the code, start the docker container using script in swarm-demo-app/docker-compose/grafana-inflix-statsd/startup.sh
Container above was taken from: https://github.com/samuelebistoletti/docker-statsd-influxdb-grafana - Please give a star to the fella who created it, as he did great job.

This will expose:

1. Grafana - available on http://localhost:3003 login credentials root/root
2. InfluxDB - available on http://localhost:3004
3. Statsd  - available on localhost port 8125

Grafana can be configured through UI to use influxDB datasource. Database name: datasource, credentials: datasource/datasource and url: http://localhost:8086
With this setup, statsd will feed data into influxDB, from which Grafana can draw nice graphs. Statsd api is expose through:

```
<dependency>
    <groupId>com.timgroup</groupId>
    <artifactId>java-statsd-client</artifactId>
    <version>${com.timgroup.statsd.version}</version>
</dependency>
```

It has very nice and simple api, and is described here: https://github.com/tim-group/java-statsd-client (hit the star button on github, nice work).

Monitoring:

On a side, it will also expose built-in healtcheck monitoring from wildfly-swarm:

1. http://localhost:8080/threads
2. http://localhost:8080/heap
3. http://localhost:8080/node

More on the above can be found in wildfly docs: https://wildfly-swarm.gitbooks.io/wildfly-swarm-users-guide/content/v/7d7ea3560e6b65f673bc76ff7fd65499e28ffca2/advanced/monitoring.html

To build run ```mvn clean install```

To run simply type (will assume defaults, which are statsdHost=localhost, statsdPort=8125 and nodeId will be random UUID). These can also be passed as jvm arguments.
 
```java -jar swarm-demo-app/restful-demo-application-ejb-cdi-jpa-metrics/target/restful-demo-application-ejb-cdi-jpa-metrics-1.0.1-SNAPSHOT-swarm.jar```

By default when login enpoint is invoked, latency will be recorded and sent to statsd/influxDb, from which you can draw new graph. Create new graph and set datasource to one named datasource, and if connection is successfull, metrics for login method will be there.
Time units are in nanoseconds.

Exposed public endpoints are:

 1. Simple public endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/public/data```
 
 Will return Content-Type: text/plain and body: Some public data
 2. Login endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/login?username=dejan&password=kitic```
 
 Will return Content-Type: text/plain and header Authorization: "Bearer JWT", where JWT is valid json web token.
 Body will be empty.
 
Exposed secured enpoints are:

1. Simple secured endpoint:
 ```HTTP GET: http://localhost:8080/api/v1.0.0/secure/data```
 
Will return 405 if Authorization header does not contain valid JWT, or will return Content-Type: text/plain and body: Some super secret data
 
### restful-demo-application-ejb-cdi-jpa-metrics-elastic ###
 
Same as examples above, this one will demonstrate ability to log metric through slf4 over rsyslog into elastic search (which is running in docker container, more on that to follow). Same endpoints
are exposed as in previous examples, and code can be ran and compiled in exactly the same manner (mvn clean install and then java -jar), no specific setup is required for integration with rsyslog, it's all done 
through wildfly swarm (or normally through jboss/EAP). To see full demo, start the docker containers found in docker-compose/elastic-kibana-rsyslog/ by running startup.sh from the same folder.

This will start up:

1. Two elastic nodes (master + node). Copy-paste node to expand in docker-compose to bring up even more.
2. Kibana, for slicing, dicing and visualising data from elastic
3. rsyslog listending on 514/udp, for easier setup
 
Once containers are running, and java -jar starts this app, triggering Login endpoint will generate latency log, which will be sent
to rsyslog, normalization rules applied in real time and stored in elastic. Kibana can then be used to dicover and visualise
data in the logs. 

Kibana is available on: http://localhost:5601/ (on the main page add index pattern enm_logs-*)

Elastic will be available on: http://localhost:9200/_plugin/hq/#cluster

rsyslog will be on udp/514, localhost again,if running with docker-compose as described above
 
### docker-compose ###

 Has couple of folders:
 
 1. elastic-kibana-rsyslog contains docker file and docker-compose with small helper script to run them
 2. grafana-influx-statsd contains docker file and compose with small helper script to run them
 
 All of these have been tested on latest stable docker and docker beta for mac, using docker-compose.

Happy hacking.