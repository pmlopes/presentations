var process = require('process');
// Load the http module to create an http server.
var express = require('express');
var pi = require('./pi');

var app = express();

app.get('/', function (req, res) {
  res.send('Request served by ' + process.pid);
});

app.get('/work', function (req, res) {
  res.send('Pi = (' + process.pid + ') ' + pi(100000000));
});

app.listen(8080, function () {
  console.log('Pi server listening on port 8080!')
});
