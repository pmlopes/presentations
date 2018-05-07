package xyz.jetdrone.paas;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.net.BindException;

public class Main extends AbstractVerticle {

  @Override
  public void start(Future<Void> future) {
    // Create a router object.
    Router app = Router.router(vertx);

    app.get("/liveness").handler(rc -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain").end("OK"));
    app.get("/readiness").handler(HealthCheckHandler.create(vertx));

    vertx.eventBus().<JsonObject>localConsumer("paas.ai", msg -> {

      final JsonObject ball = msg.body().getJsonObject("ball");
      final JsonObject paddle = msg.body().getJsonObject("paddle");

      final JsonObject response = new JsonObject();

      int x_pos = ball.getInteger("x");
      int diff = -(paddle.getInteger("x") + (paddle.getInteger("width") / 2) - x_pos);
      if (diff < 0 && diff < -4) {
        diff = -5;
      } else if (diff > 0 && diff > 4) {
        diff = 5;
      }

      response.put("diff", diff);

      if (paddle.getInteger("x") < 0) {
        response.put("x", 0);
      } else if (paddle.getInteger("x") + paddle.getInteger("width") > 400) {
        response.put("x", 400 - paddle.getInteger("width"));
      }

      msg.reply(response);
    });

    // Allow events for the designated addresses in/out of the event bus bridge
    BridgeOptions opts = new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress("paas.ai"))
        .addOutboundPermitted(new PermittedOptions().setAddress("paas.ai")).setReplyTimeout(1000);

    // Create the event bus bridge and add it to the app.
    SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
    app.route("/eventbus/*").handler(ebHandler);

    app.get().handler(StaticHandler.create());

    // Create the HTTP server and pass the "accept" method to the request handler.
    vertx.createHttpServer().requestHandler(app::accept).listen(
        // Retrieve the port from the configuration, default to 8080.
        config().getInteger("http.port", 8080), ar -> {
          if (ar.succeeded()) {
            System.out.println("Server started on port " + ar.result().actualPort());
            future.handle(ar.mapEmpty());
          } else {
            if (ar.cause() instanceof BindException) {
              System.out.println("Server already started on port " + config().getInteger("http.port", 8080));
              // ignore as we're running on an already deployed server
              future.handle(Future.succeededFuture());
            } else {
              future.handle(ar.mapEmpty());
            }
          }
        });
  }
}
