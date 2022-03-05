#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

# https://hub.docker.com/_/zookeeper/tags
ZOOKEEPER_VERSION=5.5.4
CONTAINER_NAME=shapeless_zookeeper
EXPOSED_PORT=2181
INTERNAL_PORT=2181

if [ "$(docker ps -aq -f name=^$CONTAINER_NAME\$)" ]; then
  if [ ! "$(docker ps -aq -f name=^$CONTAINER_NAME\$ -f status=exited)" ]; then
    echo "Stopping zookeeper container"
    docker stop $CONTAINER_NAME
  fi
  echo "Starting zookeeper container"
  docker start $CONTAINER_NAME
else
  echo "Creating & starting zookeeper container"
  docker run -d \
    --name $CONTAINER_NAME \
    -p $EXPOSED_PORT:$INTERNAL_PORT \
    -v ~/.shapeless_runtime/zookeeper/data:/data \
    -v ~/.shapeless_runtime/zookeeper/datalog:/datalog \
    -e ZOOKEEPER_SERVER_ID=1 \
    -e ZOOKEEPER_CLIENT_PORT=2181 \
    -e ZOOKEEPER_SERVERS="zookeeper:22888:23888" \
    confluentinc/cp-zookeeper:$ZOOKEEPER_VERSION
fi