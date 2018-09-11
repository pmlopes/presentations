package bitcoin;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
* WebSocket client for the blockchain.info service. It allows developers to receive Real-Time notifications about new
* transactions and blocks.
*/
public class BlockChainClient {

 private final Vertx vertx;

 private HttpClient client;
 private WebSocket ws;

 private final Map<String, Handler<JsonObject>> addresses = new ConcurrentHashMap<>();

 private Handler<String> status;
 private Handler<JsonObject> unconfirmed;
 private Handler<JsonObject> blocks;

 private Handler<Throwable> errorHandler;

 public static BlockChainClient create(Vertx vertx) {
   return new BlockChainClient(vertx);
 }

 private BlockChainClient(Vertx vertx) {
   this.vertx = vertx;
 }

 /**
  * Receive new transactions for a specific bitcoin address
  */
 public BlockChainClient subscribe(String address, Handler<JsonObject> handler) {
   addresses.put(address, handler);
   ws.writeFinalBinaryFrame(Buffer.buffer(new JsonObject().put("op", "addr_sub").put("addr", address).encode()));
   return this;
 }

 /**
  * Receive notifications when a new block is found. Note: if the chain splits you will receive more than one
  * notification for a specific block height
  */
 public BlockChainClient subscribeBlocks(Handler<JsonObject> handler) {
   blocks = handler;
   ws.writeFinalTextFrame(new JsonObject().put("op", "blocks_sub").encode());
   return this;
 }

 /**
  * Subscribe to notifications for all new bitcoin transactions.
  */
 public BlockChainClient subscribeUnconfirmed(Handler<JsonObject> handler) {
   unconfirmed = handler;
   ws.writeFinalTextFrame(new JsonObject().put("op", "unconfirmed_sub").encode());
   return this;
 }

 /**
  * Regardless of channel subscription you may receive status messages. This should be displayed to the user.
  */
 public BlockChainClient subscribeStatus(Handler<String> handler) {
   status = handler;
   return this;
 }

 /**
  * Register a exception handler
  *
  * @param handler the handler for runtime exceptions
  */
 public BlockChainClient exceptionHandler(Handler<Throwable> handler) {
   errorHandler = handler;
   return this;
 }

 /**
  * Connect to the service usually running at <a href="wss://ws.blockchain.info/inv">wss://ws.blockchain.info/inv</a>
  *
  * @param handler A callback handler informing the connection is complete with the self object.
  */
 public void connect(Handler<BlockChainClient> handler) {

   final BlockChainClient self = this;

   client = vertx
     .createHttpClient(new HttpClientOptions().setMaxWebsocketFrameSize(128 * 1024))
     .websocket(80, "ws.blockchain.info", "/inv", MultiMap.caseInsensitiveMultiMap().set("Host", "ws.blockchain.info"), ws -> {
       this.ws = ws;

       ws.frameHandler(frame -> {
         if (frame.isText() && frame.isFinal()) {
           JsonObject jsonFrame = new JsonObject(frame.textData());
           JsonObject msg = jsonFrame.getJsonObject("x");
           String op = jsonFrame.getString("op");

           if (status != null && "status".equals(op)) {
             status.handle(msg.getString("msg"));
             return;
           }

           Set<String> addresses = new HashSet<>();

           JsonArray inputs = msg.getJsonArray("inputs");
           if (inputs != null) {
             for (Object el : inputs) {
               JsonObject prev_out = ((JsonObject) el).getJsonObject("prev_out");
               if (prev_out != null) {
                 String addr = prev_out.getString("addr");
                 if (addr != null) {
                   addresses.add(addr);
                 }
               }
             }
           }

           JsonArray outputs = msg.getJsonArray("out");
           if (outputs != null) {
             for (Object el : outputs) {
               String addr = ((JsonObject) el).getString("addr");
               if (addr != null) {
                 addresses.add(addr);
               }
             }
           }

           for (String addr : addresses) {
             Handler<JsonObject> h = this.addresses.get(addr);
             if (h != null) {
               h.handle(msg);
             }
           }

           if (unconfirmed != null && "utx".equals(op)) {
             unconfirmed.handle(msg);
           }

           if (blocks != null && "block".equals(op)) {
             blocks.handle(msg);
           }
         }
       });

       ws.exceptionHandler(t -> {
         if (errorHandler != null) {
           errorHandler.handle(t);
         }
       });

       handler.handle(self);
     });
 }

 /**
  * Disconnect from the current service.
  */
 public void disconnect() {
   if (ws != null) {
     ws.close();
     ws = null;
   }

   if (client != null) {
     client.close();
     client = null;
   }
 }
}
