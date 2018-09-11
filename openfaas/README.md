# OpenFaaS Vert.x Native Images

This is a barebones example of Vert.x Native Images for OpenFaaS

* Bootstrap a OpenFaaS function
* Deploy the function

## Requirements

* Docker Swarm
* OpenFaaS

## Prepare OpenFaaS

```bash
docker swarm init

git clone https://github.com/openfaas/faas && \
  cd faas && \
  ./deploy_stack.sh
  
# get the login credentials
echo -n b4cc91a55fa9ecdd67c7d6a56c506b2486851a4f4ec06abefced917a6224628c | faas-cli login --username=admin --password-stdin
```

## Create a function

```bash
faas-cli template pull https://github.com/pmlopes/openfaas-svm-vertx
faas-cli new callme --lang vertx-svm
```

## Build

```bash
faas-cli build -f callme.yml
faas-cli deploy -f callme.yml
```
