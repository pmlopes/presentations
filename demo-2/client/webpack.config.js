var webpack = require('webpack');

module.exports = {
    devtool: 'source-map',
    entry: './src/main.js',
    output: {
        path: __dirname + '/../src/main/resources/static',
        filename: 'bundle.js',
        publicPath: '/'
    },
    module: {
        loaders: [{
            test: /\.js$/,
            loaders: ['babel'],
            exclude: /node_modules/
        }]
    }
};
