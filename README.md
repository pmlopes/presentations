<!-- .slide: data-background="media/withthebest.png" data-background-size="contain" -->

Notes:
  Hello everyone and welcome to "going fullstack reactive". My name is Paulo Lopes and
  I'm a Principal Software Engineer at RedHat and a core developer of the Eclipse Vert.x
  project.

---

## *What is a Reactive?*

Notes:
  Let start with the question: "What is a Reactive"?

---

## Reactive is...

* <!-- .element class="fragment" --> Message Driven
* <!-- .element class="fragment" --> Responsive

Notes:
  Google is your friend so you will find that most articles state that:

  * If you adopt a message driven development architecture,
  * And adopt a programming style around data flows, _or events if you like_

  You're reactive... _(pause)_

  And this isn't something new! Node.js is doing it for many years and we see that it is catching up on the Java side too.

---

## Reactive <span class="fragment hl-purple">Programming</span>

* Message Driven
* Responsive

Notes:
  In computer science there are only 2 hard things:

  * cache invalidation
  * naming things

  What we just describes is in fact, reactive programming. It is a programming paradigm oriented around data flows and the propagation of change. This is the programming model of `Node.js` and `Eclipse Vert.x` for example.

---

## Reactor Pattern

<!-- .element class="stretch" --> ![reactor-event-loop](media/reactor-event-loop.png)

Notes:
  The concept is simple. There is a single process/thread running an infinite loop. If picks events (from the left), for example, HTTP requests, SQL results and passes them to the correct handler (or callback if you prefer). The handler can complete the execution or produce new events to be consumed on the following iteration.

---

## Multi Reactor Pattern

<!-- .element class="stretch" --> ![multi-reactor-pattern](media/multi-reactor-pattern.png)

Notes:
  Vert.x improves the event loop pattern by implementing the **multi event loop pattern**. Which means that there will be an

---

## Reactive <span class="fragment hl-purple">System</span>

* Message Driven
* Responsive
* <!-- .element class="fragment hl-purple" -->Resilient
* <!-- .element class="fragment hl-purple" -->Elastic

Notes:
  Reactive systems as described on the reactive manifesto have 2 more traits:

  * It states that the system is resilient against failure
  * and elastic so it can scale with the load.

  Examples of projects that implements all these traits are `Akka` and `Eclipse Vert.x`.

  These 2 traits are provided by vert.x native cluster capabilities and the eventbus that allows distributed messaging, simple and to the point.

---

<!-- .element class="stretch" --> ![event-bus](media/event-bus-bridge.png)

Notes:

---

## *Why Reactive?*

<pre><code data-trim class="java" data-noescape>
@RequestMapping(value = "/work", method = RequestMethod.GET)
public @ResponseBody String work(HttpServletResponse resp) {
  resp.setContentType("text/plain");
  resp.setCharacterEncoding("UTF-8");
  <mark>return doSomeWork();</mark>
}
</code></pre>

Notes:
  It makes sense to ask, why reactive? as it sure feels like over engineering.

  Look at this code, imagine that your server runs with a thread pool of 100 elements. Now imagine that the method `doSomeWork()` does consume several seconds to complete. If you are running a popular site, you'll get more than 100 concurrent users, which means, all your user will see is:

---

<!-- .element class="stretch" --> ![loading](media/loading.gif)

Notes:
  And we all know that every person hates to wait...

---

## Page load time research say:

![1sec](media/1sec.png)
<small>*source: https://www.truconversion.com/blog/traffic/decrease-page-load-time/*</small>

Notes:
  Clearly, having a website that loads instantaneously is sort of difficult to build. So, does this mean that you are doomed?

  No. There is an allowance.

  At least 83% of people expect a page to load within three seconds or less. After this, the one second delay effects as in this infographic come into play.

  If you don’t stick to the 3-second window, then you risk an abandonment rate of over 40%.

---

## *Where to be reactive?*

* on the frontend? <span class="fragment hl-purple">**YES**</span>
* on the backend? <span class="fragment hl-purple">**YES**</span>
* both? <span class="fragment hl-purple">**YES**</span>

Notes:
  How should we solve this problem? On the frontend web application? on your backend server? Both? As you can imagine the anwser to these questions is yes, yes and yes! So let me show you one possible way, and lets start top down, front to back.

---

## Frontend <small>react.js</small>

* Simplicity
* Component Based Approach
* Great Performance and Virtual DOM
* SSR support for SEO
* Testability/Developers tools
* <!-- .element class="hl-purple" -->Bonus: Mobile apps with react native

Notes:
  Why react.js? react is a JavaScript library. It’s not a framework. It’s not a complete solution and we’ll often need to use more libraries with React to form any solution. React does not assume anything about the other parts in any full solution. It focuses on just one thing, and on doing that thing very well.

  The thing that React does really well is building User Interfaces. A User Interface is anything we put in front of users to have them interact with a machine.

---

## Backend <small>vert.x</small>

* Toolkit
* Unopinionated
* Reactive
* Polyglot
* Distributed

Notes:
  On the backend I would choose a Eclipse Vert.x. Vert.x is a toolkit, just like react.js it is not a framework, it puts developers on the front seat and does not force any opinion/pattern to solve the developer problem. It is reactive as previously illustrated, it is polyglot, it runs on the JVM but does not force you to use Java, you're not limited by what you know and can choose the best language for your problem (Java, JavaScript, Kotlin, Scala...) and above all it is distributed.

---

## To put in perspective

| (out of the box) | Spring5 | node.js | vert.x | Akka |
| ---------------- |:-------:|:-------:|:------:|:----:|
| Responsive       | ✔       | ✔      | ✔     | ✔    |
| Message Driven   |         |         | ✔     | ✔    |
| Resilient        |         |         | ✔     | ✔    |
| Elastic          |         |         | ✔     | ✔    |

Notes:
  To put things in perspective about my choices, let me illustrate what you get out of the box in relation to the reactive manifesto requirements. As you can see most modern frameworks will adopt a reactive programming style, but not all are reactive systems.

---

## *3 seconds<span class="fragment hl-purple">.</span><span class="fragment">.</span><span class="fragment hl-purple">.</span>*

Notes:
  We've drifted a bit, but lets get back, remember you've 3 seconds... or else you will start loosing your users. So let me show how to implement the backend of an hypothetical blog application.

---

<div style="display: inline-block; width: 50%; float: left;">

<div><small>
<pre style="width: 100%"><code data-trim class="java">
// Initial setup                                    //
var react = ReactSSR.createProxy(vertx);
var handlebars = HandlebarsTemplateEngine.create();
// Dummy data...
var posts = new JsonArray(
  vertx.fileSystem()
    .readFileBlocking("posts.json"));
// Route web requests to handlers...
var app = Router.router(vertx);
</code></pre></small></div>

<div class="fragment"><small>
<pre style="width: 100%"><code data-trim class="java">
// Rest API                                         //
app.get("/api/post").handler(ctx -> {
  ctx.response()
    .putHeader("content-type", "application/json")
    .end(posts.encode());
});
// Get a single post
app.get("/api/post/:id").handler(ctx -> {
  int id = Integer.parseInt(ctx.pathParam("id"));

  for (Object p : posts) {
    if (((JsonObject) p).getInteger("id") == id) {
      ctx.response()
        .putHeader("Content-Type", "application/json")
        .end(((JsonObject) p).encode());
      return;
    }
  }

  ctx.fail(404);
});
</code></pre></small></div>
</div>

<div style="display: inline-block; width: 50%;">
<div class="fragment"><small>
<pre style="width: 100%"><code data-trim class="javascript">
// Mix React.JS with Vert.x                          //
app.route().handler(ctx -> {
  react.render(ctx.request().uri(), res -> {
    if (res.failed()) {
      ctx.fail(res.cause());
    } else {
      String markup = res.result();
      if (markup == null) {
        ctx.next();
      } else {
        handlebars.render(ctx.put("markup", markup),
          "hbs/index.hbs", res1 -> {
            if (res1.failed()) {
              ctx.fail(res1.cause());
            } else {
              ctx.response()
                .putHeader("Content-Type", "text/html")
                .end(res1.result());
            }
        });
      }
    }
  });
});
</code></pre></small></div>
<div class="fragment"><small>
<pre style="width: 100%"><code data-trim class="javascript">
// Serve resources and start                         //
app.route().handler(StaticHandler.create());
// Start server
vertx.createHttpServer()
  .requestHandler(app::accept).listen(8080);
</code></pre></small></div>
</div>

<small>https://github.com/pmlopes/presentations/tree/java-withthebest</small>

---

### Deployment

<!-- .element class="stretch" --> ![monolith](media/component-monolith.png)

```sh
docker-compose start
```

---

### Deployment (Reactive <span class="hl-purple">System</span>)

<!-- .element class="stretch" --> ![monolith](media/component-cluster.png)

```sh
docker-compose scale react=2
```

---

### Deployment (Reactive <span class="hl-purple">System</span>)

<!-- .element class="stretch" --> ![monolith](media/component-trivial-cluster.png)

```sh
# or replace implementations...
```

---

<!-- .slide: data-background-video="media/serversiderendering-slow.mp4" data-background-size="contain" -->

---

## A reactive system:

* <!-- .element: class="fragment grow" --> Makes your users happy
* <!-- .element: class="fragment grow" --> Has high performance
* <!-- .element: class="fragment grow" --> Scales without code change
* <!-- .element: class="fragment grow" --> Is resilient to failure

---

<!-- .slide: style="text-align: left;" -->
# Thank you!

* https://vertx.io
* https://www.jetdrone.xyz
* https://twitter.com/pml0pes
* https://www.reactivemanifesto.org
