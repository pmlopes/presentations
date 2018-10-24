/// <reference types="@vertx/core/runtime" />
// @ts-check

// mvn clean package
// java -jar target/demo-0-1.0.0-fat.jar -cluster -ha
// java -jar target/demo-0-1.0.0-fat.jar bare
// http localhost:8000
// kill -9 pid
// see app respawn on 2nd node

vertx
  .createHttpServer()
  .requestHandler(req => {
    req
      .response()
      .end(`Request served by ${process.pid}`);
}).listen(8000);
