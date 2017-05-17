#!/bin/sh
PORT=$1
wrk -t4 -c400 -d60s http://127.0.0.1:${PORT:-8080}/work
