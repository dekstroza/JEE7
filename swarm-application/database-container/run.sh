#!/usr/bin/env bash

docker run -d --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d payments_db
