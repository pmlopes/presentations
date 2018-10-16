/// <reference types="@vertx/core/runtime" />
// @ts-check

import {Router, StaticHandler} from '@vertx/web';

const DroolsHelper = Java.type('drools.DroolsHelper');
const posts = require('./shared/posts');

import React from 'react';
import {renderToString} from 'react-dom/server';
import {match, RouterContext} from 'react-router';
import routes from './shared/components/routes';

// route all request based on the request path
const app = Router.router(vertx);

// get a drools engine
const engine = DroolsHelper.load(vertx.fileSystem().readFileBlocking("drools/rules.drl"));

app.get('/api/post').handler((ctx) => {
  ctx.response()
    .putHeader("content-type", "application/json")
    .end(JSON.stringify(posts));
});

app.get('/api/post/:id').handler((ctx) => {
  const id = parseInt(ctx.request().getParam('id'));

  const post = posts.filter(p => p.id == id);

  if (post) {
    ctx.response()
      .putHeader("content-type", "application/json")
      .end(JSON.stringify(post[0]));
  } else {
    ctx.fail(404);
  }
});


app.get('/greetings').handler(function (ctx) {
  // create a greetings message
  var greeting = DroolsHelper.createGreeting('Hello World!', function () {
    console.log('Greetings from Drools!');
  });
  // run the engine
  engine.insert(greeting);
  engine.fireAllRules();

  ctx.response().end('See the console...');
});

app.get().handler((ctx) => {
  match({routes: routes, location: ctx.request().uri()}, (err, redirect, props) => {

    if (err) {
      ctx.fail(err.message);
    } else if (redirect) {
      ctx.response()
        .putHeader("Location", redirect.pathname + redirect.search)
        .setStatusCode(302)
        .end();
    } else if (props) {
      const routerContextWithData = (
        <RouterContext
          {...props}
          createElement={(Component, props) => {
            return <Component posts={posts} {...props} />
          }}
        />
      );
      const appHtml = renderToString(routerContextWithData);

      ctx.response()
        .putHeader("content-type", "text/html")
        .end(`<!DOCTYPE html>
              <html lang="en">
              <head>
                <link rel="stylesheet" href="/wing.min.css"/>
                <meta charset="UTF-8">
                <title>Universal Blog</title>
              </head>
              <body>
                <div id="app">${appHtml}</div>
                <script src="/bundle.js"></script>
              </body>
              </html>`);
    } else {
      ctx.next();
    }
  });
});

app.get().handler(StaticHandler.create());


vertx
  // create a HTTP server
  .createHttpServer()
  // on each request pass it to our APP
  .requestHandler(function (req) {
    app.accept(req);
  })
  // listen on port 8080
  .listen(8080);
