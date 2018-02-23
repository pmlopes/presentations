package xyz.jetdrone.paas.ai;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.cluster.infinispan.InfinispanClusterManager;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import org.infinispan.health.Health;
import org.infinispan.health.HealthStatus;
import org.infinispan.manager.EmbeddedCacheManager;

public class AIVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> future) throws Exception {
    vertx.eventBus().<JsonObject>consumer("paas.ai", msg -> {

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

    // Create a router object.
    Router router = Router.router(vertx);

    router.get("/liveness").handler(rc -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain").end("OK"));
    router.get("/readiness").handler(HealthCheckHandler.createWithHealthChecks(createHealthChecks()));

    // Create the HTTP server and pass the "accept" method to the request handler.
    // The server here is only used for liveness and readiness checks
    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(
        // Retrieve the port from the configuration, default to 8080.
        config().getInteger("http.port", 8080), ar -> {
          if (ar.succeeded()) {
            System.out.println("Server started on port " + ar.result().actualPort());
          }
          future.handle(ar.mapEmpty());
        });
  }

  private HealthChecks createHealthChecks() {
    return HealthChecks.create(vertx)
      .register("ispn-cluster-status", future -> {
        VertxInternal vertxInternal = (VertxInternal) vertx;
        InfinispanClusterManager clusterManager = (InfinispanClusterManager) vertxInternal.getClusterManager();
        EmbeddedCacheManager cacheManager = (EmbeddedCacheManager) clusterManager.getCacheContainer();
        Health health = cacheManager.getHealth();
        HealthStatus healthStatus = health.getClusterHealth().getHealthStatus();
        Status status = new Status()
          .setOk(healthStatus == HealthStatus.HEALTHY)
          .setData(JsonObject.mapFrom(health));
        future.complete(status);
      });
  }
}
