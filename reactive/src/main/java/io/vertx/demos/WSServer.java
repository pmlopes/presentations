package io.vertx.demos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.*;

public class WSServer extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new WSServer());
  }

  @Override
  public void start() {

    final Router app = Router.router(vertx);

    // server does not read, only sends and sends ping every 5 minutes
    final BridgeOptions options = new BridgeOptions()
        .addOutboundPermitted(new PermittedOptions().setAddress("time"))
        .setPingTimeout(5 * 60 * 1000);

    app.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

    vertx.createHttpServer().requestHandler(app::accept).listen(8080, res -> {
      if (res.failed()) {
        throw new RuntimeException(res.cause());
      }

      // publish a new message every 10 sec
      vertx.setPeriodic(10000L, t -> {
        vertx.eventBus().publish("time", new JsonObject().put("unixtime", System.currentTimeMillis()));
      });

      System.out.println("Server ready!");
    });
  }
}
