#!/bin/sh
set -e
# export PATH=~/.local/graalvm-ce-1.0.0-rc8/bin:$PATH
# export JAVA_HOME=~/.local/graalvm-ce-1.0.0-rc8

# clean up previous runs
docker stack rm func                  || true
docker secret rm basic-auth-password  || true
docker swarm leave --force            || true
# switch to docker swarm mode
docker swarm init
# clone and install openfaas
git clone https://github.com/openfaas/faas && \
  cd faas && \
  ./deploy_stack.sh
