/// <reference types="@vertx/core/runtime" />
// @ts-check

// this example shows the basics of eventbus
// message driven development with vert.x

// npm run shell -- 1-hello.js

vertx.eventBus().consumer('events', event => {
  console.log(`Receiving ${event.body()}!`);
});

vertx.setPeriodic(1000, res => {
  vertx.eventBus().publish('events', 'hello');
});
