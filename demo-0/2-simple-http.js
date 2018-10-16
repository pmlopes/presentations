/// <reference types="@vertx/core/runtime" />
// @ts-check

// this example shows the basics of creating http servers

// npm run shell -- 2-simple-http.js
// http localhost:8000

vertx
  .createHttpServer()
  .requestHandler(req => {
    req
      .response()
      .end("Hello!!!");

  }).listen(8000, res => {
    if (res.succeeded()) {
      console.log('Server ready!');
    } else {
      console.trace(res.cause());
    }
  });
