# Lunatech

### Bringing order to the chaos with Eclipse Vert.x

<small style="font-family: 'Fira Code'">[@pml0pes](http://twitter.com/pml0pes)</small>

---

<!-- .slide: style="text-align: left;" -->
## Who am I?

* Principal Software Engineer @RedHat
* Eclipse Vert.x core developer
* Fluent in Java and JavaScript amongst others
* Game developer

Notes:
  Introduce myself, work, vertx, game developer

---

<!-- .slide: style="text-align: left;" -->
## Agenda

1. Reactive 101           <!-- .element: class="fragment grow" -->
2. Eclipse Vert.x 101     <!-- .element: class="fragment grow" -->
3. Chaos Engineering 101  <!-- .element: class="fragment grow" -->

Notes:
  This talk is a 3 in one

---

## Hands-up time

* Reactive programming  <!-- .element: class="fragment grow" -->
* Reactive systems      <!-- .element: class="fragment grow" -->
* Eclipse Vert.x        <!-- .element: class="fragment grow" -->
* Kubernetes/OpenShift  <!-- .element: class="fragment grow" -->

---

## Vert.x as a Reactive System

Notes:
  Create the plot for the story, why the audience should move
  from their current stack to Vert.x.

---

### Reactive Programming

* Thread oriented apps can't scale  <!-- .element: class="fragment grow" -->
* CPU have bugs and get slower      <!-- .element: class="fragment grow" -->
* Cloud/Containers are the trend    <!-- .element: class="fragment grow" -->
* Billing per usage                 <!-- .element: class="fragment grow" -->

---

### Quick recap on C10K

Notes:
  The C10k problem was coined in 1999 when Dan Kegel managed to sere 10.000 clients at once over 1 Gigabit Ethernet connection.

  A 1999 CPU compares give or take to a Raspberry Pi. So lets look at a variation of the C10K problem.

  Instead of just concurrent requests lets think of **websockets**. **Websockets** require you to keep
  state and the sockets open all the time.

  For an application server, this means **10K threads**.

---

<!-- .slide: data-background-video="images/c10k-2.webm" -->

---

### Reactive Programming

* Node.JS
* Spring 5
* <!-- .element: class="fragment" --> Is just **reactive programming** enough?

---

<!-- .slide: data-background="images/manifesto.jpg" data-background-size="contain" -->

Notes:
  The Reactive Manifesto is more than just Reactive Programming,
  it covers reactive programming and distributed systems

---

### Reactive Systems

* ~~Node.JS~~
* ~~Spring 5~~
* Eclipse Vert.x
* Akka, etc...

Notes:
  Let's talk about marketing lies

---

## How does Vert.x work?

* Single thread event loop*   <!-- .element: class="fragment grow" -->
* Simple message bus
* Builtin clustering
* Polyglot

---

<!-- .slide: data-background="images/nostop.gif" data-background-size="contain" -->

Notes:
  Vert.x eventloop It's not like node...

---

<!-- .slide: data-background="images/sync-workers.gif" data-background-size="contain" -->

Notes:
  Vert.x has an event loop per CPU core!

---

## How does Vert.x work?

* Single thread event loop*
* Simple message bus          <!-- .element: class="fragment grow" -->
* Builtin clustering
* Polyglot

---

#### EventBus PTP

<!-- .element: class="stretch" --> ![EventBus PTP](images/event-bus-ptp.png)

---

#### EventBus Pub-Sub

<!-- .element: class="stretch" --> ![EventBus PS](images/event-bus-ps.png)

---

#### EventBus RPC

<!-- .element: class="stretch" --> ![EventBus RPC](images/event-bus-rr.png)

---

## How does Vert.x work?

* Single thread event loop*
* Simple message bus
* Builtin clustering          <!-- .element: class="fragment grow" -->
* Polyglot

---

#### EventBus Cluster

<!-- .element: class="stretch" --> ![EventBus Cluster](images/event-bus-bridge.png)

---

## How does Vert.x work?

* Single thread event loop*
* Simple message bus
* Builtin clustering
* Polyglot                    <!-- .element: class="fragment grow" -->

---

### Java

```java
import io.vertx.core.AbstractVerticle;
public class Server extends AbstractVerticle {
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8080);
  }
}
```

---

### JavaScript

```js
vertx.createHttpServer()
  .requestHandler(function (req) {
    req.response()
      .putHeader("content-type", "text/plain")
      .end("Hello from Vert.x!");
}).listen(8080);
```

---

### Scala

```scala
import io.vertx.lang.scala.ScalaVerticle
class Server extends ScalaVerticle {
  override def start(): Unit = {
    vertx
      .createHttpServer()
     .requestHandler(_.response()
       .putHeader("content-type", "text/plain")
       .end("Hello from Vert.x"))
     .listen(8080)
  }
}
```

---

### Kotlin

```kotlin
import io.vertx.core.AbstractVerticle
class Server : AbstractVerticle() {
  override fun start() {
    vertx.createHttpServer()
      .requestHandler { req ->
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x")
      }.listen(8080)
  }
}
```

---

# PART II

---

### Imagine...

* We want a Pong As A Service app
* Web Interface
* AI is run on the back-end
* AI is updated ASAP

---

### Vert.x Starter

<a href="http://start.vertx.io/" target="_blank">Vert.x Starter - http://start.vertx.io/</a>

---

### Code Listing #1

```java
// Create a router object.
Router app = Router.router(vertx);

app.get().handler(StaticHandler.create());

// Create the HTTP server and pass the
// "accept" method to the request handler.
vertx
  .createHttpServer()
  .requestHandler(app::accept)
  .listen(
    // Retrieve the port from the configuration,
    // default to 8080.
    config().getInteger("http.port", 8080));
```

---

<!-- .slide: data-background="images/manifesto.jpg" data-background-size="contain" -->

Notes:
  Quick recap, we want a reactive system, so lets add the AI component

---

### Code Listing #2

```java
vertx.eventBus().consumer("paas.ai", msg -> {

  final JsonObject ball = msg.body().getJsonObject("ball");
  final JsonObject paddle = msg.body().getJsonObject("paddle");

  // ... AI Logic...

  final JsonObject response = new JsonObject();

  // ... more verbose java code...

  msg.reply(response);
});
```

---

### Code Listing #3

```js
eventBus.registerHandler("paas.ai", msg => {

  let ball = msg.body.ball;
  let paddle = msg.body.paddle;

  // ... AI Logic...

  let response = { ... };

  // ... more node.js code...

  msg.reply(response);
});
```

---

# DEMO

---

# PART III

---

<!-- .element: class="stretch" --> ![Chaos Engineering](images/production.jpg)

---

### Principles of Chaos Engineering

* <!-- .element: class="fragment grow" --> Build a Hypothesis around Steady State Behavior
* <!-- .element: class="fragment grow" --> Vary Real-world Events
* <!-- .element: class="fragment grow" --> Run Experiments in **Production**
* <!-- .element: class="fragment grow" --> Automate Experiments to Run Continuously
* <!-- .element: class="fragment grow" --> Minimize Blast Radius

---

### Real World events

* [<span class="fragment">?</span>] Server down
* [<span class="fragment">?</span>] Network issues
* [<span class="fragment">?</span>] DDoS
* [<span class="fragment">?</span>] Popularity

Notes:
  The first 2 can be simulated with chaos tools, the last ones with load testing tools like wrk

---

### Going to Production

<!-- .element: class="stretch" --> ![No Panic!](images/no-panic.jpg)

---

# DEMO

Notes:
  Deploy to OpenShift

---

### Run Experiments in Production

* https://github.com/netflix/chaosmonkey
* <!-- .element: class="fragment grow" --> https://github.com/gaia-adm/pumba
* ...

---

### Important commands

```sh
# Kill randomly containers
pumba --random --interval 30s \
  kill --signal SIGKILL \
  re2:.*POD-paas-ai.*

# Lose packets every 3s+/-30ms
pumba netem --interface eth0 \
  delay --time 3000 --jitter 30 --correlation 20 \
  re2:.*paas.*
```

---

# DEMO

---

### Minimize Blast Radius

* [<span class="fragment">X</span>] scale horizontally
* [<span class="fragment">X</span>] scale across data centers
* [<span class="fragment">X</span>] use circuit breakers
* <!-- .element: class="fragment" -->[X] **embrace failure**

---

<!-- .slide: style="text-align: left;" -->
# Thank you!

* https://www.jetdrone.xyz
* https://twitter.com/pml0pes
* https://github.com/vert-x3
* https://www.reactivemanifesto.org
* http://principlesofchaos.org
* https://github.com/dastergon/awesome-chaos-engineering
