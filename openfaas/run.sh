#!/bin/sh
set -e
export PATH=~/.local/graalvm-ce-1.0.0-rc6/bin:$PATH
export JAVA_HOME=~/.local/graalvm-ce-1.0.0-rc6

docker stack rm func                  || true
docker secret rm basic-auth-password  || true
docker swarm leave --force            || true

docker swarm init
cd faas
./deploy_stack.sh
