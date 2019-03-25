package vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class APIClientVerticle extends AbstractVerticle {


  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new APIClientVerticle());
  }

  @Override
  public void start() {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setSsl(true).setTrustAll(true));

    client
      .get(443, "icanhazdadjoke.com", "/")
      .putHeader("Accept", "text/plain")
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          System.out.println("Got HTTP response with status " + response.statusCode() + " with data "
              + response.body().toString("ISO-8859-1"));
        } else {
          ar.cause().printStackTrace();
        }
    });
  }
}
