module.exports = {
    entry: "./src/main/react/index.js",
    output: {
        filename: "./src/main/resources/webroot/bundle.js"
    },
    devtool: 'source-map',
    module: {
        loaders: [
            { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader' }
        ]
    }
};
