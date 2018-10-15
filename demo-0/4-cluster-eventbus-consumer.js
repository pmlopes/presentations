/// <reference types="@vertx/core/runtime" />
// @ts-check

// this example shows clustering

// npm run shell -- -g -- -cluster 4-cluster-eventbus-consumer.js
// npm run shell -- -g -- -cluster 4-cluster-eventbus-producer.js

vertx.eventBus()
  .consumer(
    'events',
    m => {
      console.log(`Receiving ${m.body()}`);
    });
