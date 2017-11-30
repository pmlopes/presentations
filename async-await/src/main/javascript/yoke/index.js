function Router() {
    this.middleware = [];
  }
  
  Router.prototype.use = function (fn) {
    this.middleware.push(fn)
  }
  
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
      next()
        .then(function () {
          console.error('End of chain!');
          if (!request.isEnded()) {
            request.response().end();
          }
        })
        .catch(function (ex) {
          console.trace(ex);
        });
    });
  
    return server;
  };
  
  module.exports = Router;