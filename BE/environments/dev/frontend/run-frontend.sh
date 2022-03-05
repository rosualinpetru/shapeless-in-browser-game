#!/bin/bash

script_dir="$PWD/$(dirname "$0")"
script_dir=${script_dir//\/\./}
# shellcheck disable=SC2164
cd "$script_dir"

BE_ROOT="$script_dir/../../.."
FE_ROOT="$BE_ROOT/../FE"
GENERATED="$BE_ROOT/.generated"
GENERATED_FE="$GENERATED/frontend"

CONTAINER_NAME="shapeless_frontend"
EXPOSED_PORT=31700
INTERNAL_PORT=80

mkdir -p "$GENERATED"

echo "Building frontend derivation!"
nix-build --out-link "$GENERATED_FE" \
  --argstr feRoot "$FE_ROOT" \
  --argstr version "$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 6 | head -n 1)" \
  --argstr nginxConf "$script_dir/configs/nginx.conf" \
  --option sandbox-paths "$HOME=$HOME" \
  nix/frontend.nix

echo "Building frontend image!"
docker build -f dockerfiles/Frontend -t "shapeless/frontend" --build-arg REACT_APP_REMOTE="shapeless.go.ro" "$GENERATED_FE"

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  if [ ! "$(docker ps -aq -f name=$CONTAINER_NAME -f status=exited)" ]; then
    echo "Stopping frontend container"
    docker stop "$CONTAINER_NAME"
  fi
  docker rm "$CONTAINER_NAME"
fi
echo "Creating & starting frontend container"
docker run -d \
  --name "$CONTAINER_NAME" \
  -p "$EXPOSED_PORT:$INTERNAL_PORT" \
  "shapeless/frontend"

docker ps