const path = require('path');

let vertxConfig = {

  entry: path.resolve(__dirname, 'server.js'),

  output: {
    filename: './index.js'
  },

  module: {
    loaders: [
      { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader' }
    ]
  }
};

let webConfig = {

  entry: path.resolve(__dirname, 'client.js'),

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
