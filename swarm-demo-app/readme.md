# Simple rest endpoint exposed using Wildfly Swarm #

### Requirements ###

1. Java 8
2. Maven (min 3.3.9)
3. Docker (recent installation with working docker-compose)

### Content ###
Demo application exposing few rest endpoints, some of which are secured with JWT for demonstration purposes. Application is packed and deployed using (Wildfly Swarm)[http://wildfly-swarm.io]
Project contains three maven modules, each building more on top of previous one, and repetition of code across all three of them is intentional.

Maven modules included are:

1. restful-demo-application
2. restful-demo-application-ejb-cdi-jpa
3. restful-demo-application-ejb-cdi-jpa-metrics

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
This example also demonstrates use of consule for service topology, so before running consul should be started. On OSX consul can be installed using brew, with: ```brew install consul``` and then we can run it with:```consul agent -dev -bind IP_ADDRESS``` and it will be also
available at ```http://localhost:8500``` Once consul agent is up, provide information about it when running jar:

To build run ```mvn clean install```

To run simply type (assuming consule is up and running, available on http://localhost:8500): 
```java -jar -Djava.net.preferIPv4Stack=true -Dswarm.consul.url="http://127.0.0.1:8500" swarm-demo-app/restful-demo-application-ejb-cdi-jpa/target/restful-demo-application-ejb-cdi-jpa-1.0.1-SNAPSHOT-swarm.jar```

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

Features same as above, but not requiring consul - this one demonstrates us of grafana, statsd and influxdb for performance monitoring.
Before running the code, start the docker container using script in swarm-demo-app/docker-compose/grafana-inflix-statsd/startup.sh
Container above was taken from: https://github.com/samuelebistoletti/docker-statsd-influxdb-grafana - Please give a star to the fella who created it, as he did great job.

This will expose:

1. Grafana - available on http://localhost:3003 login credentials root/root
2. InfluxDB - available on http://localhost:3004
3. Statsd  - available on localhost port 8125

Grafana can be configured through UI to use influxDB datasource, database name datasource, credentials: datasource/datasource and url: http://localhost:8086
With this setup, statsd will feed data into influxDB, from which Grafana can draw nice graphs.

On a side, it will also expose built-in healtcheck monitoring from wildfly-swarm:

1. http://localhost:8080/threads
2. http://localhost:8080/heap
3. http://localhost:8080/node

To build run ```mvn clean install```

To run simply type (will assume defaults, which are statsdHost=localhost, statsdPort=8125 and nodeId will be random UUID). These can also be passed as jvm arguments. 
```java -jar -Djava.net.preferIPv4Stack=true -Dswarm.consul.url="http://127.0.0.1:8500" swarm-demo-app/restful-demo-application-ejb-cdi-jpa/target/restful-demo-application-ejb-cdi-jpa-1.0.1-SNAPSHOT-swarm.jar```

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
 
 
### docker-compose ###
 
Has folder called elastic-kibana-rsyslog in which you will find startup.sh. Running this script will start several containers:
 
1. elastic master
2. three elastic nodes
3. kibana
4. rsyslog
 
Elastic will be available on: http://localhost:9200/_plugin/hq/#cluster
Kibana is available on: http://localhost:5601/app/kibana
And last, but not least you can test rsyslog->elastic with:
```docker exec -it rsyslog logger "SomeMetric,15,ROP1,STATS,NODETYPE1,1220"```

it will index it inside index called enm_logs-*: 

Happy hacking.