# Vertx.NL Meetup #2

## Code demos

### slow

Compare JavaScript on Vert.x vs Node

```
mvn clean package
java -jar target/slow-0.0.1-fat.jar
wrk -t12 -c400 -d30s http://127.0.0.1:8080

cd src/main/node
node index.js
wrk -t12 -c400 -d30s http://127.0.0.1:3000
```

### reactive

Compare RAM usage

```
sudo ./run-server.sh
sudo ./run-client.sh
sudo jps
sudo ../ps_mem.py -p pid

sudo ./run-server.sh
sudo ./run-node.sh
sudo ps -ef|grep "node index.js"
sudo ../ps_mem.py -p pid
```

### ssr

Mix Server Side Rendering with Vert.x code

```
npm i
webpack
java -jar target/ssr-0.0.1-fat.jar
```

### IDE

IDE support with proper code completion

```
npm run vertx.d
# the IDE should now properly give type hints
# add the comment
# /// <reference types="vertx-runtime" />
# the IDE will properly know about the default globals
```

### async-await

Demo using async await (based on Promises ES7)

```
webpack
java -jar target/async-await-0.0.1-fat.jar
http localhost:8080
```

### debug

show how to use the Java Debugger to debug javascript (requires IntelliJ IDEA)

```
create runner: io.vertx.core.Launcher run main.js
http localhost:8080/api
stop, start in debug
http localhost:8080/api
stop
npm test
```