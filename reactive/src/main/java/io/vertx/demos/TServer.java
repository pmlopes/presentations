package io.vertx.demos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class TServer extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new TServer());
  }

  @Override
  public void start() {

    final Router app = Router.router(vertx);

    app.get().handler(ctx -> {
      vertx.setTimer(60000, t -> {
        ctx.response()
          .putHeader("Content-Type", "text/plain")
          .end("Hello!");
      });
    });

    vertx.createHttpServer().requestHandler(app::accept).listen(8080, res -> {
      if (res.failed()) {
        throw new RuntimeException(res.cause());
      }
      System.out.println("Server ready!");

      new Thread(() -> {
        for (int i = 0; i < 10 * 1024; i++) {
          new Thread(() -> {
            while (true) {
              try {
                URLConnection conn = new URL("http://localhost:8080/").openConnection();
                try (InputStream in = conn.getInputStream()) {
                  int ch;
                  while ((ch = in.read()) != -1) {
                    System.out.print((char) ch);
                  }
                  System.out.println();
                }
                Thread.sleep(1000);
              } catch (InterruptedException | IOException e) {
                System.out.println(e.getMessage());
              } catch (OutOfMemoryError e) {
                e.printStackTrace(System.err);
                System.exit(1);
              }
            }
          }).start();
        }
      }).start();
    });
  }
}

