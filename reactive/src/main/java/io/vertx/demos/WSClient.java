package io.vertx.demos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.JsonObject;

public class WSClient extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new WSClient());
  }

  private static final String PING = new JsonObject().put("type", "ping").encode();
  private static final String SUBSCRIBE_TIME = new JsonObject().put("type", "register").put("address", "time").encode();

  @Override
  public void start() {
    // 10k problem, no big deal!
    final WebSocket[] sockets = new WebSocket[20 * 1024];

    for (int i = 0; i < sockets.length; i++) {
      final int id = i;

      vertx.createHttpClient().websocket(8080, "localhost", "/eventbus/websocket", ws -> {
        sockets[id] = ws;
        System.out.println("Connected " + id);

        ws.frameHandler(frame -> System.out.println("Received message: " + frame.binaryData()));

        ws.exceptionHandler(Throwable::printStackTrace);

        ws.endHandler(v -> {
          System.err.println("Connection ended: " + id);
          sockets[id] = null;
        });

        // subscribe to the "time" address
        ws.writeFrame(WebSocketFrame.textFrame(SUBSCRIBE_TIME, true));
      });
    }

    // keep the socket open by sending ping messages every 2:30 min
    vertx.setPeriodic(5 * 60 * 1000 / 2, t -> {
      for (WebSocket socket : sockets) {
        socket.writeFrame(WebSocketFrame.textFrame(PING, true));
      }
    });
  }
}
