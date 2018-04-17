package withthebest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import withthebest.react.ReactSSR;

public class Web extends AbstractVerticle {

  @Override
  public void start() {
    // create a react server side renderer
    ReactSSR react = ReactSSR.createProxy(vertx);
    HandlebarsTemplateEngine handlebars = HandlebarsTemplateEngine.create();
    // load the posts
    JsonArray posts = new JsonArray(vertx.fileSystem().readFileBlocking("posts.json"));
    Router app = Router.router(vertx);

    app.get("/api/post").handler(ctx -> {
      ctx.response()
        .putHeader("content-type", "application/json")
        .end(posts.encode());
    });

    app.get("/api/post/:id").handler(ctx -> {

      int id = Integer.parseInt(ctx.pathParam("id"));

      for (Object p : posts) {
        if (((JsonObject) p).getInteger("id") == id) {
          ctx.response()
            .putHeader("content-type", "application/json")
            .end(((JsonObject) p).encode());
          return;
        }
      }

      ctx.fail(404);
    });

    app.route().handler(ctx -> {
      react.render(ctx.request().uri(), res -> {
        if (res.failed()) {
          ctx.fail(res.cause());
        } else {
          String markup = res.result();
          if (markup == null) {
            ctx.next();
          } else {
            ctx.put("markup", markup);
            handlebars.render(ctx, "hbs/index.hbs", res1 -> {
              if (res1.failed()) {
                ctx.fail(res1.cause());
              } else {
                ctx.response()
                  .putHeader("Content-Type", "text/html")
                  .end(res1.result());
              }
            });
          }
        }
      });
    });

    app.route().handler(StaticHandler.create());
    vertx.createHttpServer().requestHandler(app::accept).listen(8080);
  }
}
