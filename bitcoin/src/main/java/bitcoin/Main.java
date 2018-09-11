package bitcoin;

import io.reactiverse.pgclient.*;
import io.reactiverse.pgclient.data.Json;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class Main {

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    final EventBus eb = vertx.eventBus();

    final PgPool postgres = PgClient.pool(
      vertx,
      new PgPoolOptions()
        .setHost("postgres")
        .setUser("dbuser")
        .setPassword("dbpassword")
        .setDatabase("bitcoin"));

    BlockChainClient.create(vertx)
      .exceptionHandler(Throwable::printStackTrace)
      .connect(self -> {
        self.subscribeUnconfirmed(json -> {
          postgres.preparedQuery("INSERT INTO UTX (data) VALUES ($1)", Tuple.of(Json.create(json)), ar -> {
            if (ar.succeeded()) {
              int utxValue = 0;
              if (json.containsKey("out")) {
                // reduce step
                for (Object o : json.getJsonArray("out")) {
                  utxValue += ((JsonObject) o).getInteger("value", 0);
                }
              }
              eb.publish("data.updates", utxValue);
            } else {
              ar.cause().printStackTrace();
            }
          });
        });
      });

    final Router app = Router.router(vertx);

    app.route("/eventbus/*")
      .handler(SockJSHandler.create(vertx)
        .bridge(new BridgeOptions()
          .addOutboundPermitted(new PermittedOptions().setAddress("data.updates"))));

    app.route().handler(StaticHandler.create("static"));

    vertx.createHttpServer().requestHandler(app)
        .listen(Integer.getInteger("port", 8080), listen -> {
          if (listen.succeeded()) {
            System.out.println("Server started at: http://localhost:" + Integer.getInteger("port", 8080));
          } else {
            listen.cause().printStackTrace();
            vertx.close();
          }
        });
  }
}
