#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

PSQL_SH="postgresql.sh"

mkdir -p ~/.shapeless_runtime/zookeeper/data
mkdir -p ~/.shapeless_runtime/zookeeper/datalog
mkdir -p ~/.shapeless_runtime/kafka/data

./$PSQL_SH

docker ps