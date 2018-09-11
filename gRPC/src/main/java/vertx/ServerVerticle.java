package vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;

public class ServerVerticle extends AbstractVerticle {

  @Override
  public void start() {
    VertxServer server = VertxServerBuilder.forAddress(vertx, "127.0.0.1", 50051).addService(new GreeterGrpc.GreeterVertxImplBase() {
      @Override
      public void sayHello(HelloRequest request, Future<HelloReply> future) {
        System.out.println("Hello " + request.getName());
        future.complete(HelloReply.newBuilder().setMessage("Hi there, " + request.getName()).build());
      }
    }).build();
    server.start(ar -> {
      if (ar.succeeded()) {
        System.out.println("gRPC service started");
      } else {
        System.out.println("Could not start server " + ar.cause().getMessage());
        ar.cause().printStackTrace();
      }
    });
  }

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new ServerVerticle());
  }
}
