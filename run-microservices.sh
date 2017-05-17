#!/bin/sh
cd microservices
webpack
mvn clean install
java -jar target/things-0.0.1-SNAPSHOT-fat.jar