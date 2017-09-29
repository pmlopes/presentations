package xyz.jetdrone.services;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

@ProxyGen
public interface PiService {

  static PiService create(Vertx vertx) {
    return new PiServiceImpl(vertx);
  }

  void calculatePi(Handler<AsyncResult<String>> handler);
}
