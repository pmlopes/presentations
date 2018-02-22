const path = require('path');
const VertxPlugin = require('webpack-vertx-plugin');
const npm = require('./package.json');

module.exports = [
  // client side
  {
    entry: path.resolve(__dirname, 'client.js'),
    devtool: 'source-map',

    output: {
      filename: './webroot/bundle.js'
    },

    module: {
      loaders: [
        {test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader'}
      ]
    }
  },
  // server side
  {
    entry: path.resolve(__dirname, 'server.js'),
    target: 'node',

    output: {
      filename: './index.js'
    },

    module: {
      loaders: [
        { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader' }
      ]
    },

    plugins: [
      new VertxPlugin({
        verbose: true,
        verticle: npm.main
      })
    ]
  }
];
