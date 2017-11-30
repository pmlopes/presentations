/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__webpack_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 2);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


module.exports = function () {
    return new Promise(function (resolve, reject) {
        // fake that we go to a database and then...

        // 50% - 50% change
        if (Math.random() > 0.5) {
            /* everything turned out fine */
            resolve("Data from the database!");
        } else {
            /* that didn't went well */
            reject(Error("It broke"));
        }
    });
};

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


function Router() {
  this.middleware = [];
}

Router.prototype.use = function (fn) {
  this.middleware.push(fn);
};

Router.prototype.accept = function (server) {
  var self = this;
  server.requestHandler(function (request) {
    var idx = 0;

    var next = function next() {
      return self.middleware[idx++](context);
    };

    var context = {
      request: request,
      response: request.response(),
      next: next
    };

    // start
    next().then(function () {
      console.error('End of chain!');
      if (!request.isEnded()) {
        request.response().end();
      }
    }).catch(function (ex) {
      console.trace(ex);
    });
  });

  return server;
};

module.exports = Router;

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var Yoke = __webpack_require__(1);
var dbCall = __webpack_require__(0);

var app = new Yoke();

app.use(function (ctx) {
  var start, ms;
  return Promise.resolve().then(function () {
    console.log('------------------------');
    // Step 1
    start = new Date();

    console.log('Started at: ' + start);
    // Step 2
    return Promise.resolve().then(function () {
      return ctx.next();
    }).catch(function (e) {
      // there's a 50% change of failure!
      ctx.response.statusCode = 500;
      ctx.response.end('Oopsy!');
    }).then(function () {
      // Step 7
      ms = new Date() - start;

      console.log('Elapsed: ' + ms + 'ms');
    }, function (_err) {
      return Promise.resolve().then(function () {
        ms = new Date() - start;
        console.log('Elapsed: ' + ms + 'ms');throw _err;
      });
    });
  }).then(function () {});
});

app.use(function (ctx) {
  return Promise.resolve().then(function () {
    // Step 3
    console.log('Hey, I\'m just another middleware');
    // Step 4
    return ctx.next();
  }).then(function () {
    // Step 6
    console.log('More stuff after body has been set');
  });
});

app.use(function (ctx) {
  var name;
  return Promise.resolve().then(function () {
    return dbCall();
  }).then(function (_resp) {
    name = _resp;
    // Step 5

    ctx.response.end('Hello ' + name);
    console.log('Body has been set');
  });
});

app.accept(vertx.createHttpServer()).listen(8080);
console.log("Server listening @ http://localhost:8080/");

/***/ })
/******/ ]);