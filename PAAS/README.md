# Run the examples

## Local development

Build:

```bash
mvn clean package
```

Run locally (bare metal):

```bash
mvn vertx:run
```

### AI Module (from Node.JS)

```bash
cd node-ai
```

Build:

```bash
npm install
```

Run locally (bare metal):

```bash
npm start
```

## OpenShift development

### Start a local OpenShift instance

```bash
oc cluster up
```

### Deploy the modules

```bash
mvn clean fabric8:deploy
```

### Play with Pumba

Once pumba is installed on your system you can simulate module crashes:

```bash
pumba --random --interval 1m kill --signal SIGKILL "re2:.*paas.*"
```

Or network errors:

```bash
# packet loss
pumba --debug netem --duration 1m --tc-image gaiadocker/iproute2 loss -p 20 -c 10 "re2:.*paas.*"
# slow network
pumba --debug netem --duration 1m --tc-image gaiadocker/iproute2 delay --time 500 "re2:.*paas.*"
# introduce latency
pumba --debug netem --duration 1m --tc-image gaiadocker/iproute2 delay \
    --time 100 \
    --jitter 30 \
    --correlation 20 \
    "re2:.*paas.*"
```
