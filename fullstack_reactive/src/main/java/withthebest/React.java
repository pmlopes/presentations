package withthebest;

import io.vertx.core.AbstractVerticle;
import withthebest.react.ReactSSR;

public class React extends AbstractVerticle {

  @Override
  public void start() {
    // create a react server side renderer
    ReactSSR.registerService(vertx);
  }
}
