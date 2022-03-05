#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

docker stop "$(docker ps --format "{{.ID}}" --filter "name=shapeless_postgres")"