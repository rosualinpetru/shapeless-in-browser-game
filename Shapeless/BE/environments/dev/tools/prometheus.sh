#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

# https://hub.docker.com/r/prom/prometheus/tags
PROM_VERSION=v2.26.0
CONTAINER_NAME=shapeless_prometheus
EXPOSED_PORT=9090
INTERNAL_PORT=9090

if [ "$(docker ps -aq -f name=^$CONTAINER_NAME\$)" ]; then
	if [ ! "$(docker ps -aq -f name=^$CONTAINER_NAME\$ -f status=exited)" ]; then
		echo "Stopping prometheus container"
		docker stop $CONTAINER_NAME
	fi
	echo "Starting prometheus container"
	docker start $CONTAINER_NAME
else
	echo "Creating & starting prometheus container"
	docker run -d \
		--name $CONTAINER_NAME \
		-p $EXPOSED_PORT:$INTERNAL_PORT \
		-v prometheus:/etc/prometheus \
		prom/prometheus:$PROM_VERSION
fi