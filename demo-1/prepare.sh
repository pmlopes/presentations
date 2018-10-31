#!/bin/sh
set -e
# export PATH=~/.local/graalvm-ce-1.0.0-rc8/bin:$PATH
# export JAVA_HOME=~/.local/graalvm-ce-1.0.0-rc8

docker swarm leave --force || true
docker run --rm -p 5432:5432 techempower/postgres
