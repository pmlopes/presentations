# Vert.x Native Images

This is a barebones example of Vert.x Native Images

* The application will connect to a database server (localhost:5432)
* The application will open a websocket to ws.blockchain.info
* For each unconfirmed transaction it will write it to Postgres
* For each successful write it will publish a message on the event bus
* The application will start a HTTP server on port 8080
* Accept websocket connections and bridge the eventbus to browsers
* A client side app will render the transactions in realtime
* The color changes according to the ammount of satoshi's in the UTX

## Requirements

* GraalVM 1.0.0-rc3 in the PATH
* JAVA_HOME pointing to GraalVM 1.0.0-rc3
* Node.js, NPM

```
npm install -g vertx-scripts
```

## Building Images

### From scratch

Before anything is done we need a `pom.xml`

```sh
vertx-script init -b com.example.MainClass
```

Where `com.example.MainClass` is your main class, you cannot use the Launcher!!!

### Building images

With the initial `pom.xml` just add dependencies as usual and build the image:

```sh
vertx-scripts native-image -v
```

The final binary will be generated in the `target` directory. You will observe
that 2 files are added to your project:

```
src/main/svm/reflection.json
src/main/svm/substitutions.java
```

These files are helpers to workaround current issues with substrate VM, if your
code trigger new issues you can use those to workaround your issues.

## Other stuff

### Build the client application

```sh
cd client
npm install
./node_modules/.bin/webpack
```

### Starting a Test DB

```sh
docker-compose build
docker-compose up
```

