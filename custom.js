// replace the default notes plugin
options.dependencies[5].src = './plugin/notes-server/client.js';
// add socket io
options.dependencies.unshift({ "src": "//cdn.socket.io/socket.io-1.3.5.js", "async": true });
