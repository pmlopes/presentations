package rx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.*;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {

    final Router router = Router.router(vertx);
    // Allow events for the designated addresses in/out of the event bus bridge
    BridgeOptions sockjsConfig = new BridgeOptions()
      .addInboundPermitted(new PermittedOptions().setAddress("greetings"))
      .addOutboundPermitted(new PermittedOptions().setAddress("greetings"));
    // Create the event bus bridge and add it to the router.
    router
      .route("/eventbus/*")
      .handler(SockJSHandler.create(vertx).bridge(sockjsConfig));

    router.route().handler(StaticHandler.create());
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    EventBus eb = vertx.eventBus();

    vertx.setPeriodic(500, t ->
      eb.send("greetings",
        new JsonObject().put("msg", "Greetings from Vert.x!")));
  }
}
