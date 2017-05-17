package codemotion;

import java.lang.management.ManagementFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import math.*;

public class MainVerticle extends AbstractVerticle {

  private static final String PID =
    ManagementFactory.getRuntimeMXBean().getName();

  @Override
  public void start() {

    final Router app = Router.router(vertx);

    // responsive, even under high load this handler
    // can respond to requests
    app.get("/").handler(ctx -> {
      ctx.response()
        .putHeader("content-type", "text/plain")
        // resilient, in case of failure the system
        // recovers
        .end("Current PID: " + PID);
    });

    app.get("/work").handler(ctx -> {
      // message driven, events can cross boundaries
      // using a message driven architecture
      vertx.eventBus().send("calculatePi", null, reply -> {
        if (reply.failed()) {
          ctx.fail(reply.cause());
        } else {
          ctx.response()
            .putHeader("content-type", "text/plain")
            .end("Pi = " + reply.result().body());
        }
      });
    });

    // elastic we can deploy several consumers to scale
    // the application
    vertx.eventBus().consumer("calculatePi", m -> {
      // can mix blocking and non-blocking code
      vertx.executeBlocking(
        fut -> fut.complete(Pi.calculate(100_000_000)),
        false,
        fut -> m.reply("(" + PID + ") " + fut.result()));
    });

    vertx.createHttpServer().requestHandler(app::accept).listen(8080, ar ->{
      if (ar.succeeded()) {
        System.out.println("Server listening at http://localhost:8080");
      }
    });
  }
}
