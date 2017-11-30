vertx
  .createHttpServer()
  .requestHandler(function (req) {
    req.response().end(JSON.stringify({msg: 'Hello World!'}))
  })
  .listen(8080);

console.log('Server listening: http://127.0.0.1:8080/');
