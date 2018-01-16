package xyz.jetdrone.paas.ai;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class AIVerticle extends AbstractVerticle {

  @Override
  public void start() {
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
  }
}
