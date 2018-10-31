package com.example.techempower;

import io.reactiverse.pgclient.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import io.vertx.ext.web.Router;

public class TechEmpower extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new TechEmpower(args[0]));
  }

  private TechEmpower(String server) {
    this.server = server == null ? "vertx" : server;
  }

  private final String server;
  private String date;

  /**
   * Returns the value of the "queries" getRequest parameter, which is an integer
   * bound between 1 and 500 with a default value of 1.
   *
   * @param request the current HTTP request
   * @return the value of the "queries" parameter
   */
  private static int getQueries(HttpServerRequest request) {
    String param = request.getParam("queries");

    if (param == null) {
      return 1;
    }
    try {
      int parsedValue = Integer.parseInt(param);
      return Math.min(500, Math.max(1, parsedValue));
    } catch (NumberFormatException e) {
      return 1;
    }
  }

  /**
   * Returns a random integer that is a suitable value for both the {@code id}
   * and {@code randomNumber} properties of a world object.
   *
   * @return a random world number
   */
  private static int randomWorld() {
    return 1 + ThreadLocalRandom.current().nextInt(10000);
  }

  @Override
  public void start() {

    final Router app = Router.router(vertx);

    vertx.setPeriodic(1000, handler -> date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));

    /*
     * This test exercises the framework fundamentals including keep-alive support, request routing, request header
     * parsing, object instantiation, JSON serialization, response header generation, and request count throughput.
     */
    app.get("/json").handler(ctx -> {
      ctx.response()
        .putHeader("Server", server)
        .putHeader("Date", date)
        .putHeader("Content-Type", "application/json")
        .end(new JsonObject().put("message", "Hello, World!").toBuffer());
    });

    final String SELECT_WORLD = "SELECT id, randomnumber from WORLD where id=$1";

    PgClient client = PgClient.pool(
      vertx,
      new PgPoolOptions()
        .setUser("benchmarkdbuser")
        .setPassword("benchmarkdbpass")
        .setDatabase("hello_world"));

    /*
     * This test exercises the framework's object-relational mapper (ORM), random number generator, database driver,
     * and database connection pool.
     */
    app.get("/db").handler(ctx -> {
      client.preparedQuery(SELECT_WORLD, Tuple.of(randomWorld()), res -> {
        if (res.succeeded()) {
          PgIterator resultSet = res.result().iterator();

          if (!resultSet.hasNext()) {
            ctx.fail(404);
            return;
          }

          Row row = resultSet.next();

          ctx.response()
            .putHeader("Server", server)
            .putHeader("Date", date)
            .putHeader("Content-Type", "application/json")
            .end(new JsonObject().put("id", row.getInteger(0)).put("randomNumber", row.getInteger(1)).toBuffer());
        } else {
          ctx.fail(res.cause());
        }
      });
    });

    /*
     * This test is a variation of Test #2 and also uses the World table. Multiple rows are fetched to more dramatically
     * punish the database driver and connection pool. At the highest queries-per-request tested (20), this test
     * demonstrates all frameworks' convergence toward zero requests-per-second as database activity increases.
     */
    app.get("/queries").handler(ctx -> {
      final AtomicBoolean failed = new AtomicBoolean(false);
      JsonArray worlds = new JsonArray();

      final int queries = getQueries(ctx.request());

      for (int i = 0; i < queries; i++) {
        client.preparedQuery(SELECT_WORLD, Tuple.of(randomWorld()), ar -> {
          if (!failed.get()) {
            if (ar.failed()) {
              failed.set(true);
              ctx.fail(ar.cause());
              return;
            }

            // we need a final reference
            final Row row = ar.result().iterator().next();
            worlds.add(new JsonObject().put("id", row.getInteger(0)).put("randomNumber", row.getInteger(1)));

            // stop condition
            if (worlds.size() == queries) {
              ctx.response()
                .putHeader("Server", server)
                .putHeader("Date", date)
                .putHeader("Content-Type", "application/json")
                .end(worlds.toBuffer());
            }
          }
        });
      }
    });


    /*
     * This test is an exercise of the request-routing fundamentals only, designed to demonstrate the capacity of
     * high-performance platforms in particular. Requests will be sent using HTTP pipelining. The response payload is
     * still small, meaning good performance is still necessary in order to saturate the gigabit Ethernet of the test
     * environment.
     */
    app.get("/plaintext").handler(ctx -> {
      ctx.response()
        .putHeader("Server", server)
        .putHeader("Date", date)
        .putHeader("Content-Type", "text/plain")
        .end("Hello, World!");
    });

    vertx
      .createHttpServer()
      .requestHandler(app)
      .listen(8080, listen -> {
        if (listen.succeeded()) {
          System.out.println("Server listening at: http://localhost:8080/");
        } else {
          listen.cause().printStackTrace();
        }
      });
  }
}
