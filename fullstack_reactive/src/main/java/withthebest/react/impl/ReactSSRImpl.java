package withthebest.react.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import jdk.nashorn.api.scripting.JSObject;
import withthebest.react.ReactSSR;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ReactSSRImpl implements ReactSSR {

  static {
    // enable ES6 features
    if (System.getProperty("nashorn.args") == null) {
      System.setProperty("nashorn.args", "--language=es6");
    }
  }

  private final JSObject vertxSSR;

  public ReactSSRImpl(Vertx vertx) {
    try {
      // create a engine instance
      ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
      // load script
      engine
        .eval(vertx.fileSystem().readFileBlocking("ssr.js").toString());
      // reference the function
      vertxSSR = (JSObject) engine.get("vertxSSR");
    } catch (ScriptException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void render(String path, Handler<AsyncResult<String>> handler) {
    RenderHandler cb = (err, redirect, markup) -> {
      if (err != null) {
        handler.handle(Future.failedFuture((String) err.getMember("message")));
        return;
      }

      // todo handle redirects...

      handler.handle(Future.succeededFuture(markup));
    };

    vertxSSR.call(null, path, cb);
  }
}
