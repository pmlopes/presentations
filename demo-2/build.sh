#!/bin/sh
set -e
export PATH=~/.local/graalvm-ce-1.0.0-rc6/bin:$PATH
export JAVA_HOME=~/.local/graalvm-ce-1.0.0-rc6

./mvnw -Pnative-image
./target/bitcoin-viewer
