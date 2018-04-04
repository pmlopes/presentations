# Going Fullstack React(ive)!
<small style="font-family: 'Fira Code'">@pml0pes</small>

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
  And we all know that every human being hates to wait...

---

## Page load time research say:

![1sec](media/1sec.png)
<small>*source: https://www.truconversion.com/blog/traffic/decrease-page-load-time/*</small>

Notes:
  Clearly, having a website that loads instantaneously is sort of difficult. So, does this mean that you are doomed?

  No. There is an allowance.

  At least 83% of people expect a page to load within three seconds or less. After this, the one second delay effects as in this infographic come into play.

  If you don’t stick to the 3-second window, then you risk an abandonment rate of over 40%.

---

## *How to be reactive?*

* on the frontend? <span class="fragment hl-purple">**YES**</span>
* on the backend? <span class="fragment hl-purple">**YES**</span>
* both? <span class="fragment hl-purple">**YES**</span>

---

## Frontend <small>react.js</small>

* Simplicity
* Component Based Approach
* Great Performance and Virtual DOM
* SSR support for SEO
* Testability/Developers tools
* <!-- .element class="hl-purple" -->Bonus: Mobile apps with react native

Notes:
  React is a JavaScript library. It’s not a framework. It’s not a complete solution and we’ll often need to use more libraries with React to form any solution. React does not assume anything about the other parts in any full solution. It focuses on just one thing, and on doing that thing very well.

  The thing that React does really well is building User Interfaces. A User Interface is anything we put in front of users to have them interact with a machine.

---

## Backend <small>vert.x</small>

* Toolkit
* Unopinionated
* Reactive
* Polyglot
* Distributed

---

## To put in perspective

| (out of the box) | Spring5 | node.js | vert.x |
| ---------------- |:-------:|:-------:|:------:|
| Responsive       | ✔       | ✔      | ✔     |
| Message Driven   |         |         | ✔     |
| Resilient        |         |         | ✔     |
| Elastic          |         |         | ✔     |

Notes:

---

## *3 seconds<span class="fragment">.</span><span class="fragment">.</span><span class="fragment">.</span>*

Notes:

---

<div style="display: inline-block; width: 50%; float: left;">

<div><small>
<pre style="width: 100%"><code data-trim class="javascript">
// Initial Setup                               //
import {Router, StaticHandler} from '@vertx/web'

import React from 'react'
import {renderToString} from 'react-dom/server'
import {match, RouterContext} from 'react-router'
import routes from '../shared/components/routes'

const app = Router.router(vertx)
</code></pre></small></div>

<div class="fragment"><small>
<pre style="width: 100%"><code data-trim class="javascript">
// Rest API (Similar to Express.JS)            //
app.get('/api/post').handler((ctx) => {
  ctx.response()
    .putHeader("content-type", "application/json")
    .end(JSON.stringify(posts))
})

app.get('/api/post/:id').handler((ctx) => {
  const id = ctx.request().getParam('id')
  const post = posts.filter(p => p.id == id)
  if (post) {
    ctx.response()
      .putHeader(
          "content-type", "application/json")
      .end(JSON.stringify(post[0]))
  } else {
    ctx.fail(404)
  }
})
</code></pre></small></div>
</div>

<div style="display: inline-block; width: 50%;">
<div class="fragment"><small>
<pre style="width: 100%"><code data-trim class="javascript">
// Mix React.JS with Vert.x                    //
app.get().handler((ctx) => {
  match({
      routes: routes,
      location: ctx.request().uri()
  }, (err, redirect, props) => {

    if (err) {
      ctx.fail(err.message);
    } else if (redirect) {
      ctx.response()
        .putHeader("Location",
          redirect.pathname + redirect.search)
        .setStatusCode(302)
        .end();
    } else if (props) {
      const routerContextWithData = (
        &lt;RouterContext
          {...props}
          createElement={(Component, props) => {
            return &lt;Component
                posts={posts} {...props} /&gt;
          }}
        /&gt;)
</code></pre></small></div>
</div>

---

<div style="display: inline-block; width: 50%; float: left;">
<div><small>
<pre style="width: 100%"><code data-trim data-no-escape class="javascript">
// Render React.js without Node                //
      const appHtml =
          renderToString(routerContextWithData)

      ctx.response()
        .putHeader("content-type", "text/html")
        .end(\`&lt;!DOCTYPE html&gt;
              &lt;html lang="en"&gt;
              &lt;head&gt;
                &lt;meta charset="UTF-8"&gt;
                &lt;title&gt;Universal Blog&lt;/title&gt;
              &lt;/head&gt;
              &lt;body&gt;
                &lt;div id="app"&gt;${appHtml}&lt;/div&gt;
                &lt;script
                    src="/bundle.js"&gt;&lt;/script&gt;
              &lt;/body&gt;
              &lt;/html&gt;\`)
    } else {
      ctx.next()
    }
  })
})
</code></pre></small></div>
<div class="fragment"><small>
<pre style="width: 100%"><code data-trim class="javascript">
// Serve resources and start                   //
app.get().handler(StaticHandler.create())

vertx.createHttpServer()
    .requestHandler(app).listen(8080)
</code></pre></small></div>
</div>

<div style="display: inline-block; width: 50%;">
<h2 class="fragment">DEMO</h2>
</div>

---

<!-- .slide: data-background-video="media/serversiderendering-slow.mp4" data-background-size="contain" -->

---

## A reactive system...

* Makes your users happy
* Has high performance
* Scales without code change
* Is resilient to failure

---

<!-- .slide: style="text-align: left;" -->
# Thank you!

* https://vertx.io
* https://www.jetdrone.xyz
* https://twitter.com/pml0pes
* https://www.reactivemanifesto.org
