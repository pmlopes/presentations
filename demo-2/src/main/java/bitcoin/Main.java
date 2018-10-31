package bitcoin;

import io.reactiverse.pgclient.*;
import io.reactiverse.pgclient.data.Json;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GithubAuth;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class Main {

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    final EventBus eb = vertx.eventBus();

    final String CLIENT_ID = "b906f794e1d6229e8721"; //System.getenv("CLIENT_ID");
    final String CLIENT_SECRET = "e7f7b24e1d8b304c66285ad168c69b95835c0432"; //System.getenv("CLIENT_SECRET");

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

    // Simple auth service which uses a GitHub to
    // authenticate the user
    OAuth2Auth authProvider =
      GithubAuth.create(vertx, CLIENT_ID, CLIENT_SECRET, new HttpClientOptions().setTrustAll(true));

    // We need cookies and sessions
    app.route()
      .handler(CookieHandler.create())
      .handler(SessionHandler.create(LocalSessionStore.create(vertx)))
      // We need a user session handler too to make sure
      // the user is stored in the session between requests
      .handler(UserSessionHandler.create(authProvider));

    // the Oauth2 callback path
    Route callback = app.route("/callback");

    app.route()
      // we now protect the everything
      .handler(OAuth2AuthHandler.create(authProvider)
          // we now configure the oauth2 handler, it will
          // setup the callback handler
          // as expected by your oauth2 provider.
          .setupCallback(callback))
      // serve the webapp
      .handler(StaticHandler.create());

    // configure SSL
    HttpServerOptions httpOptions = new HttpServerOptions()
      .setSsl(true)
      .setKeyStoreOptions(new JksOptions()
        .setPath("certificates.keystore")
        .setPassword("localhost"));

    // start the server
    vertx.createHttpServer(httpOptions).requestHandler(app)
        .listen(8443, listen -> {
          if (listen.succeeded()) {
            System.out.println("Server started at: https://localhost:8443");
          } else {
            listen.cause().printStackTrace();
            vertx.close();
          }
        });
  }
}
