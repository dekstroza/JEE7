#!/bin/bash

ELASTIC=$(getent hosts es_master | awk '{ print $1 }');echo $ELASTIC
sed -i 's/HOST/'$ELASTIC'/g' /config-dir/logstash.conf

cat /config-dir/logstash.conf

logstash -f /config-dir/logstash.conf

