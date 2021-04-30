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
rm -rf "$GENERATED_FE"
nix-store --delete /nix/store/*-shapeless-frontend* 2> /dev/null
nix-build --out-link "$GENERATED_FE" \
  --argstr feRoot "$FE_ROOT" \
  --argstr nginxConf "$script_dir/configs/nginx.conf" \
  nix/frontend.nix

echo "Building frontend image!"
LOCAL_REACT_APP_API_URL="http://localhost:31500/"
docker build -f dockerfiles/Frontend -t "$CONTAINER_NAME" --build-arg REACT_APP_API_URL="$LOCAL_REACT_APP_API_URL" "$GENERATED_FE"

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  if [ ! "$(docker ps -aq -f name=$CONTAINER_NAME -f status=exited)" ]; then
    echo "Stopping frontend container"
    docker stop "$CONTAINER_NAME"
  fi
  echo "Starting frontend container"
  docker start "$CONTAINER_NAME"
else
  echo "Creating & starting frontend container"
  docker run -d \
    --name "$CONTAINER_NAME" \
    -p "$EXPOSED_PORT:$INTERNAL_PORT" \
    "$CONTAINER_NAME"
fi

docker ps