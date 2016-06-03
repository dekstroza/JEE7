# Instructions

## Compile and run

Before running code, start containers by running elastic-kibana-rsyslog/startup.sh from docker-compose directory located in the root of the project.


```
mvn clean install // will compile code
java -jar target/restful-demo-application-ejb-cdi-jpa-metrics-elastic-1.0.1-SNAPSHOT-swarm.jar // will run it
```

To trigger a log entry run:

```
curl -v "http://localhost:8080/api/v1.0.0/login?username=dejan&password=kitic"
```

Rsyslog will contain log ```"Jun  2 14:02:23 - swarm-demo-app[20500] ﻿applicationLogin->11632232 ns."``` which will be forwarded into elastic.

Elastic is accessible at: http://localhost:9200/_plugin/hq/#
Kibana is accessible at: http://localhost:5601
rsyslog is listening on port 514/udp
