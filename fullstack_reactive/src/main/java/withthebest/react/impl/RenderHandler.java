package withthebest.react.impl;

import jdk.nashorn.api.scripting.JSObject;

@FunctionalInterface
public interface RenderHandler {
  void handle(JSObject err, JSObject redirect, String markup);
}
