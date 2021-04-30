#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

BE_ROOT="$script_dir/../../.."
GENERATED_BE="$BE_ROOT/.generated/backend"

echo "Building backend derivation!"
rm -rf "$GENERATED_BE"
nix-store --delete /nix/store/*-shapeless-backend* 2> /dev/null
nix-build --out-link "$GENERATED_BE" --argstr beRoot "$BE_ROOT" nix/backend.nix

echo "Building backend images!"
docker build -f "dockerfiles/Dispatcher" -t "shapeless/dispatcher" "$GENERATED_BE/dispatcher"


mkdir -p ~/.shapeless_runtime/zookeeper/data
mkdir -p ~/.shapeless_runtime/zookeeper/datalog
mkdir -p ~/.shapeless_runtime/kafka/data
docker-compose -p shapeless -f configs/docker-compose.yaml up --remove-orphans