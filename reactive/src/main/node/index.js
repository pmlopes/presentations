const WebSocket = require('ws');

const PING = {type: 'ping'};
const SUBSCRIBE_TIME = {type: 'register', address: 'time'};
const len = 10 * 1024;
const sockets = [];

for (var i = 0; i < len; i++) {
  const id = i;

  const ws = new WebSocket('ws://localhost:8080/eventbus/websocket');
  sockets[id] = ws;

  ws.on('open', function () {
    console.log('Connected ' + id);
    // subscribe to the "time" address
    ws.send(JSON.stringify(SUBSCRIBE_TIME));
  });

  ws.on('message', function (frame) {
    console.log(JSON.stringify(frame));
  });

  ws.on('error', function (error) {
    console.log(error);
  });

  ws.on('close', function () {
    console.log('Connection ended: ' + id);
    sockets[id] = null;
  });
}

// keep the socket open by sending ping messages every 2:30 min
setInterval(function () {
  for (var i = 0; i < len; i++) {
    if (sockets[i]) {
      sockets[i].send(JSON.stringify(PING));
    }
  }
}, 5 * 60 * 1000 / 2);
