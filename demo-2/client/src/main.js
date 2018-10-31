import EventBus from 'vertx3-eventbus-client'
import {getColorForTxValue, getRandomInt} from './util.js'
import Line from './line.js'

// Initialize the context of the canvas
const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");

// Set the canvas width and height to occupy full window
const W = window.innerWidth - 20,
      H = 320;

canvas.width = W;
canvas.height = H;

const lineCount = 20,
  lines = [];

// Create Lines
for (let i = 0; i < lineCount; i++) {
  lines.push(new Line(canvas, i));
}

(function loop() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  for (let i = 0; i < lines.length; i++) {
    lines[i].drawDots(ctx);
    lines[i].moveDots();
  }

  requestAnimationFrame(loop);
})();

const eb = new EventBus(`//${location.host}/eventbus`);

eb.onopen = () => {
  eb.registerHandler('data.updates', (err, msg) => {
    if (!err) {
      let w = 10 + Math.round((msg.body) / 10000000);
      if (w > 100) {
        w = 100;
      }

      lines[getRandomInt(0, lineCount - 1)]
        .pushDot(w, getColorForTxValue(msg.body));
    }
  });
};
