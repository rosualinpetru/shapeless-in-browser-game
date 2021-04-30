#!/bin/bash

# This script ensures that eny exported *_FILE env var is exported as a independent variable
# This is the convention kept by imaged on Dockerhub. Used for swarm secrets.
# https://medium.com/@adrian.gheorghe.dev/using-docker-secrets-in-your-environment-variables-7a0609659aab

set -e

file_env() {
   local var="$1"
   local fileVar="${var}_FILE"
   local def="${2:-}"
   local val="$def"
   if [ "${!var:-}" ]; then
      val="${!var}"
   elif [ "${!fileVar:-}" ]; then
      val="$(< "${!fileVar}")"
   fi
   export "$var"="$val"
   unset "$fileVar"
}

file_env "SHP_FB_CLIENT_ID"
file_env "SHP_FB_CLIENT_SECRET"
file_env "SHP_DISPATCHER_POSTGRES_DATABASE_NAME"
file_env "SHP_DISPATCHER_POSTGRES_USERNAME"
file_env "SHP_DISPATCHER_POSTGRES_PASSWORD"
file_env "SHP_DISPATCHER_POSTGRES_SCHEMA"

java -jar "$1.jar"