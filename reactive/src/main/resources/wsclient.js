var WebSocketFrame = require('vertx-js/web_socket_frame');

var PING = JSON.stringify({type: 'ping'});
var SUBSCRIBE_TIME = JSON.stringify({type: 'register', address: 'time'});
var len = 10; //10 * 1024;
var sockets = [];

// 10k problem, no big deal!
for (var i = 0; i < len; i++) {
  var id = i;

  vertx.createHttpClient().websocket(8080, "localhost", "/eventbus/websocket", function (ws) {
    sockets[id] = ws;
    console.log("Connected " + id);

    ws.frameHandler(function (frame) {
      console.log("Received message: " + frame.binaryData());
    });

    ws.exceptionHandler(function (e) {
      console.error(e.printStackTrace());
    });

    ws.endHandler(function () {
      console.log("Connection ended: " + id);
      sockets[id] = null;
    });

    // subscribe to the "time" address
    ws.writeFrame(WebSocketFrame.textFrame(SUBSCRIBE_TIME, true));
  });
}

// keep the socket open by sending ping messages every 2:30 min
setInterval(function () {
  for (var i = 0; i < len; i++) {
    if (sockets[i]) {
      sockets[i].writeFrame(WebSocketFrame.textFrame(PING, true));
    }
  }
}, 5 * 60 * 1000 / 2);
