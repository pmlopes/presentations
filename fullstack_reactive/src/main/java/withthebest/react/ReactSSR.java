package withthebest.react;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import withthebest.react.impl.ReactSSRImpl;

@VertxGen
@ProxyGen
public interface ReactSSR {

  /**
   * The default service address.
   */
  String DEFAULT_ADDRESS = "withthebest.reactssr";

  /**
   * Method called to create a proxy (to consume the service).
   *
   * @param vertx   vert.x
   * @return the proxy
   */
  @GenIgnore
  static ReactSSR createProxy(Vertx vertx) {
    return new ServiceProxyBuilder(vertx)
      .setAddress(DEFAULT_ADDRESS)
      .build(ReactSSR.class);
  }

  @GenIgnore
  static void registerService(Vertx vertx) {
    new ServiceBinder(vertx)
      .setAddress(DEFAULT_ADDRESS)
      .setTimeoutSeconds(30)
      .register(ReactSSR.class, new ReactSSRImpl(vertx));
  }

  void render(String path, Handler<AsyncResult<String>> handler);
}
