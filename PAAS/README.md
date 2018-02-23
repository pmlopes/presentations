# Run the examples

## Local development

### Web Module

```
cd pass-web
```

Build:

```
mvn clean package
```

Run locally (bare metal):

```
mvn vertx:run
```

### AI Module

```
cd pass-ai
```

Build:

```
mvn clean package
```

Run locally (bare metal):

```
mvn vertx:run
```

### AI Module (from Node.JS)

```
cd node-ai
```

Build:

```
npm install
```

Run locally (bare metal):

```
npm start
```

## OpenShift development

### Start a local OpenShift instance

```
oc cluster up
# the vert.x app needs to access the OC cluster info
# so we need to give the deployment/runtime user the
# required permissions
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)
```

### Deploy the modules

```
mvn clean fabric8:deploy
```

### Play with Pumba

Once pumba is installed on your system you can simulate module crashes:

```
pumba --random --interval 1m kill --signal SIGKILL re2:.*paas.*
```

Or network errors:

```
pumba netem --duration 5m --interface eth0 delay \
      --time 3000 \
      --jitter 30 \
      --correlation 20 \
    re2:.*paas.*
```
