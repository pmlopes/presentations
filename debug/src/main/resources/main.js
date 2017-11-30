const Router = require("vertx-web").Router;
const StaticHandler = require("vertx-web").StaticHandler;

const app = Router.router(vertx);

app.get('/api').handler(function (ctx) {
  ctx.response()
    .putHeader("content-type", "application/json")
    .end(JSON.stringify({"json": true}));
});

app.get().handler(StaticHandler.create());

vertx.createHttpServer().requestHandler(function (req) { app.accept(req); }).listen(8080);

console.log('Server listening: http://127.0.0.1:8080/');
