"use strict";

const EventBus = require('vertx3-eventbus-client');

const eventBus = new EventBus('http://localhost:8080/eventbus', {
  vertxbus_reconnect_attempts_max: Infinity,  // Max reconnect attempts
  vertxbus_reconnect_delay_min: 1000,         // Initial delay (in ms) before first reconnect attempt
  vertxbus_reconnect_delay_max: 5000,         // Max delay (in ms) between reconnect attempts
  vertxbus_reconnect_exponent: 2,             // Exponential backoff factor
  vertxbus_randomization_factor: 0.5          // Randomization factor between 0 and 1
});

eventBus.enableReconnect(true);

eventBus.onopen = function () {

  console.log('Connected!');

  eventBus.registerHandler("paas.ai", (err, msg) => {

    if (err) {
      console.error(err);
      return;
    }

    let ball = msg.body.ball;
    let paddle = msg.body.paddle;

    let response = {};

    let x_pos = ball.x;
    let diff = -1.0 * (paddle.x + (paddle.width / 2.0) - x_pos);
    if (diff < 0 && diff < -4) {
      diff = -5;
    } else if (diff > 0 && diff > 4) {
      diff = 5;
    }

    response.diff = diff;

    if (paddle.x < 0) {
      response.x = 0;
    } else if (paddle.x + paddle.width > 400) {
      response.x = 400 - paddle.width;
    }

    msg.reply(response);
  });
};
