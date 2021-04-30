#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

PSQL_SH="postgresql.sh"
PROM_SH="prometheus.sh"
ZOO_SH="zookeeper.sh"
KAFKA_SH="kafka.sh"

mkdir -p ~/.shapeless_runtime/zookeeper/data
mkdir -p ~/.shapeless_runtime/zookeeper/datalog
mkdir -p ~/.shapeless_runtime/kafka/data

./$PSQL_SH
./$PROM_SH
./$ZOO_SH
./$KAFKA_SH

docker ps