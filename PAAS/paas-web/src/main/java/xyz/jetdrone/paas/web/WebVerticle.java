package xyz.jetdrone.paas.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class WebVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> future) {
    // Create a router object.
    Router app = Router.router(vertx);

    // Allow events for the designated addresses in/out of the event bus bridge
    BridgeOptions opts = new BridgeOptions()
      .addInboundPermitted(new PermittedOptions().setAddress("paas.ai"))
      .addOutboundPermitted(new PermittedOptions().setAddress("paas.ai"))
      .setReplyTimeout(1000);

    // Create the event bus bridge and add it to the app.
    SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
    app.route("/eventbus/*").handler(ebHandler);

    app.get().handler(StaticHandler.create());

    // Create the HTTP server and pass the "accept" method to the request handler.
    vertx
      .createHttpServer()
      .requestHandler(app::accept)
      .listen(
        // Retrieve the port from the configuration, default to 8080.
        config().getInteger("http.port", 8080), ar -> {
          if (ar.succeeded()) {
            System.out.println("Server started on port " + ar.result().actualPort());
          }
          future.handle(ar.mapEmpty());
        });

  }
}
