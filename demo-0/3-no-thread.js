/// <reference types="@vertx/core/runtime" />
// @ts-check

// this example shows that there are no extra threads

// npm run shell -- 3-no-thread.js

const Thread = Java.type('java.lang.Thread');

console.log(`1) Thread : ${Thread.currentThread().getName()}`);

add(1, 1, res => {
  console.log(`3) Thread : ${Thread.currentThread().getName()} result: ${res}`);
})

console.log('2) After');

function add(a, b, handler) {
  let r = a + b;
  // schedule to execute immediatly after this cycle
  setImmediate(() => {
    handler(r);
  })
}
