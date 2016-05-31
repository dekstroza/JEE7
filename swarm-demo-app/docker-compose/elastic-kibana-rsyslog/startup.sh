#!/bin/bash

docker-compose up -d 
sleep 20
echo "Please wait till elastic HQ plugin installs..."

docker exec es_master plugin install royrusso/elasticsearch-HQ

