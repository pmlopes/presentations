const path = require('path');
const bundleOutputDir = './src/main/resources';
const VertxPlugin = require('webpack-vertx-plugin');

let vertxConfig = {

  entry: path.resolve(__dirname, 'src/main/javascript/server.js'),

  output: {
    filename: './src/main/resources/main.js'
  },

  module: {
    loaders: [
      { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader' }
    ]
  },

  plugins: [
    new VertxPlugin()
  ]
};

module.exports = [vertxConfig];
