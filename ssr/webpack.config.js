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
    new VertxPlugin({
      fatJar: 'target/demo-0.0.1-fat.jar'
    })
  ]
};

let webConfig = {

  entry: path.resolve(__dirname, 'src/main/javascript/client.js'),

  devtool: 'source-map',

  output: {
    filename: './src/main/resources/webroot/bundle.js'
  },

  module: {
    loaders: [
      { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader' }
    ]
  }
};

module.exports = [vertxConfig, webConfig];
