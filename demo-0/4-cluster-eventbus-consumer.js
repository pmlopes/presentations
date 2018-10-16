/// <reference types="@vertx/core/runtime" />
// @ts-check

// this example shows clustering

// npm run shell -- 4-cluster-eventbus-consumer.js -- -cluster
// npm run shell -- 4-cluster-eventbus-producer.js -- -cluster

vertx.eventBus()
  .consumer(
    'events',
    m => {
      console.log(`Receiving ${m.body()}`);
    });
