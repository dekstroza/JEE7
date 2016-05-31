#!/bin/bash

## Set up elastic server uri ##
sed -i 's/SERVER/'$ELASTIC_SERVER'/g' /etc/rsyslog.d/10_elasticsearch.conf

## Start rsyslog ###
/etc/init.d/rsyslog start

while true; do 
sleep 15;
done

