package xyz.jetdrone;

import java.lang.management.ManagementFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.serviceproxy.ProxyHelper;
import xyz.jetdrone.services.PiService;

public class MainVerticle extends AbstractVerticle {

  private static final String PID =
    ManagementFactory.getRuntimeMXBean().getName();

  @Override
  public void start() {

    final Router app = Router.router(vertx);
    final PiService pi = ProxyHelper
      .createProxy(PiService.class, vertx, "pi");

    app.get("/").handler(ctx -> {
      ctx.response()
        .end("Current PID: " + PID);
    });

    app.get("/work").handler(ctx -> {
      pi.calculatePi(ar -> {
        if (ar.failed()) {
          ctx.fail(ar.cause());
        } else {
          ctx.response()
            .end("Pi = " + ar.result());
        }
      });
    });

    ProxyHelper.registerService(
      PiService.class,
      vertx,
      PiService.create(vertx),
      "pi");

    vertx.createHttpServer()
      .requestHandler(app::accept)
      .listen(8080, ar ->{
        if (ar.succeeded()) {
          System.out.println("Server listening at http://localhost:8080");
        }
    });
  }
}
