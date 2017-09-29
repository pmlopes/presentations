package xyz.jetdrone.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import math.Pi;

import java.lang.management.ManagementFactory;

public class PiServiceImpl implements PiService {

  final String PID = ManagementFactory.getRuntimeMXBean().getName();

  private final Vertx vertx;

  public PiServiceImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public void calculatePi(Handler<AsyncResult<String>> handler) {
    // can mix blocking and non-blocking code
    vertx.executeBlocking(
            fut -> fut.complete(PID + ": " + Pi.calculate(100000000)),
            false,
            handler);
  }
}
