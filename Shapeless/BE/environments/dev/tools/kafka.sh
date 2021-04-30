#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

# https://hub.docker.com/r/confluentinc/cp-kafka/tags
KAFKA_VERSION=5.5.4
CONTAINER_NAME=shapeless_kafka
EXPOSED_PORT=9092
INTERNAL_PORT=9092


if [ "$(docker ps -aq -f name=^$CONTAINER_NAME\$)" ]; then
  if [ ! "$(docker ps -aq -f name=^$CONTAINER_NAME\$ -f status=exited)" ]; then
    echo "Stopping kafka container"
    docker stop $CONTAINER_NAME
  fi
  echo "Starting kafka container"
  docker start $CONTAINER_NAME
else
  echo "Creating & starting kafka container"
  docker run -d \
    --name $CONTAINER_NAME \
    -p $EXPOSED_PORT:$INTERNAL_PORT \
    -v ~/.shapeless_runtime/kafka/data:/var/lib/kafka/data \
    -e KAFKA_ADVERTISED_LISTENERS="LISTENER_DOCKER_INTERNAL://kafka:19092,LISTENER_DOCKER_EXTERNAL://${DOCKER_HOST_IP:-localhost}:9092" \
    -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP="LISTENER_DOCKER_INTERNAL:PLAINTEXT,LISTENER_DOCKER_EXTERNAL:PLAINTEXT" \
    -e KAFKA_INTER_BROKER_LISTENER_NAME="LISTENER_DOCKER_INTERNAL" \
    -e KAFKA_ZOOKEEPER_CONNECT="zookeeper:2181" \
    -e KAFKA_BROKER_ID=1 \
    -e KAFKA_LOG4J_LOGGERS="kafka.controller=INFO,kafka.producer.async.DefaultEventHandler=INFO,state.change.logger=INFO" \
    confluentinc/cp-kafka:$KAFKA_VERSION
fi
