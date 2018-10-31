#!/bin/bash
server=${SERVER:-localhost:8080}
json=${JSON:-/json}
plaintext=${PLAINTEXT:-/plaintext}
# not all frameworks implement this
db=${DB:-/db}
queries=${QUERIES:-/queries}

echo "--------------------------"
echo "Benchmark setup"
echo "--------------------------"
echo "Server:    http://${server}"
echo "PLAINTEXT: http://${server}${plaintext}"
echo "JSON:      http://${server}${json}"
[ ! -z "${db}" ]       && echo "DB:        http://${server}${db}"
[ ! -z "${queries}" ]  && echo "QUERIES:   http://${server}${queries}"
echo "--------------------------"
echo ""

if [ ! -z "${json}" ]; then
    echo "--------------------------"
    echo "Running JSON Serialization"
    echo "--------------------------"
    echo "Warming up..."
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 8 --timeout 8 -t 8 http://${server}${json} > /dev/null
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${json} > /dev/null
    echo "Running..."
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 8 --timeout 8 -t 8 http://${server}${json}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 16 --timeout 8 -t 8 http://${server}${json}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 32 --timeout 8 -t 8 http://${server}${json}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 64 --timeout 8 -t 8 http://${server}${json}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 128 --timeout 8 -t 8 http://${server}${json}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${json}
fi

if [ ! -z "${db}" ]; then
    echo "--------------------------"
    echo "Running Single Query"
    echo "--------------------------"
    echo "Warming up..."
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 8 --timeout 8 -t 8 http://${server}${db} > /dev/null
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${db} > /dev/null
    echo "Running..."
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 8 --timeout 8 -t 8 http://${server}${db}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 16 --timeout 8 -t 8 http://${server}${db}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 32 --timeout 8 -t 8 http://${server}${db}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 64 --timeout 8 -t 8 http://${server}${db}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 128 --timeout 8 -t 8 http://${server}${db}
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${db}
fi

if [ ! -z "${queries}" ]; then
    echo "--------------------------"
    echo "Running Multiple Queries"
    echo "--------------------------"
    echo "Warming up..."
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 8 --timeout 8 -t 8 http://${server}${queries}?queries=2 > /dev/null
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${queries}?queries=2 > /dev/null
    echo "Running..."
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${queries}?queries=1
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${queries}?queries=5
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${queries}?queries=10
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${queries}?queries=15
    wrk -H "Host: localhost" -H "Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${queries}?queries=20
fi

if [ ! -z "${plaintext}" ]; then
    echo "--------------------------"
    echo "Running Plaintext"
    echo "--------------------------"
    echo "Warming up..."
    wrk -H "Host: localhost" -H "Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 16384 --timeout 8 -t 8 http://${server}${plaintext} > /dev/null
    wrk -H "Host: localhost" -H "Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 16384 --timeout 8 -t 8 http://${server}${plaintext} > /dev/null
    echo "Running..."
    wrk -H "Host: localhost" -H "Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 256 --timeout 8 -t 8 http://${server}${plaintext} -s ./pipeline.lua -- 16
    wrk -H "Host: localhost" -H "Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 1024 --timeout 8 -t 8 http://${server}${plaintext} -s ./pipeline.lua -- 16
    wrk -H "Host: localhost" -H "Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 4096 --timeout 8 -t 8 http://${server}${plaintext} -s ./pipeline.lua -- 16
    wrk -H "Host: localhost" -H "Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7" -H "Connection: keep-alive" --latency -d 15 -c 16384 --timeout 8 -t 8 http://${server}${plaintext} -s ./pipeline.lua -- 16
fi
