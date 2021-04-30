#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

# https://hub.docker.com/_/postgres?tab=tags
POSTGRES_VERSION=13.2
CONTAINER_NAME=shapeless_postgres
EXPOSED_PORT=5432
INTERNAL_PORT=5432
MAX_CONNECTIONS=500

DB_NAME=shapeless
DB_USER=shapeless
DB_PASS=shapeless

if [ "$(docker ps -aq -f name=^$CONTAINER_NAME\$)" ]; then
  if [ ! "$(docker ps -aq -f name=^$CONTAINER_NAME\$ -f status=exited)" ]; then
    echo "Stopping postgres container"
    docker stop $CONTAINER_NAME
  fi
  echo "Starting postgres container"
  docker start $CONTAINER_NAME
else
  echo "Creating & starting postgres container"
  docker run -d \
    --name $CONTAINER_NAME \
    -p $EXPOSED_PORT:$INTERNAL_PORT \
    -e POSTGRES_DB=$DB_NAME \
    -e POSTGRES_USER=$DB_USER \
    -e POSTGRES_PASSWORD=$DB_PASS \
    postgres:$POSTGRES_VERSION \
    postgres -N $MAX_CONNECTIONS
fi
