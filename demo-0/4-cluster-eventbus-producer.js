/// <reference types="@vertx/core/runtime" />
// @ts-check

vertx.eventBus()
  .publish(
    'events',
    // message
    'hello from es4x producer');
