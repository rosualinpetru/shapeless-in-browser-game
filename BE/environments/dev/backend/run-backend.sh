#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

BE_ROOT="$script_dir/../../.."
GENERATED_BE="$BE_ROOT/.generated/backend"

echo "Building backend derivation!"
echo "$GENERATED_BE"
nix-build --out-link "$GENERATED_BE" --argstr version "$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 6 | head -n 1)" --argstr beRoot "$BE_ROOT" --option sandbox false nix/backend.nix

echo "Building backend images!"
docker build -f "dockerfiles/Dispatcher" -t "shapeless/dispatcher" "$GENERATED_BE/dispatcher"
docker build -f "dockerfiles/Designer" -t "shapeless/designer" "$GENERATED_BE/designer"

docker-compose -p shapeless -f configs/docker-compose.yaml up --remove-orphans