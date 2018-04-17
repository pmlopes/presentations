module.exports = [
  {
    entry: './src/main/js/server/index.js',
    output: {
      path: __dirname + '/src/main/resources',
      filename: 'ssr.js',
      library: 'vertxSSR'
    },
    module: {
      rules: [
        {test: /\.(js)$/, use: 'babel-loader'}
      ]
    }
  },
  {
    entry: './src/main/js/client/index.js',
    output: {
      path: __dirname + '/src/main/resources/webroot',
      filename: 'main.js'
    },
    module: {
      rules: [
        {test: /\.(js)$/, use: 'babel-loader'}
      ]
    }
  }
];
