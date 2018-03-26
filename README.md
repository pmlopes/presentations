## Turning Reactive into Gold

#### Reactive Amsterdam Meetup 2018

<small style="font-family: 'Fira Code'">[@pml0pes](http://twitter.com/pml0pes)</small>

---

<!-- .slide: style="text-align: left;" -->
## Who am I?

* Principal Software Engineer @RedHat
* Eclipse Vert.x core developer
* Fluent in Portuguese, Java, JavaScript, etc...

Notes:
  Introduce myself, work, vertx

---

<!-- .slide: style="text-align: left;" -->
## Agenda

1. <!-- .element: class="fragment grow" --> **Bockchain** <small>the cool parts</small>
2. <!-- .element: class="fragment grow" --> Eclipse **Vert.x**
3. <!-- .element: class="fragment grow" --> **Reactive** Blockchain FTW!

---

## Hands-up time

* <!-- .element: class="fragment grow" --> Blockchain
* <!-- .element: class="fragment grow" --> Reactive programming
* <!-- .element: class="fragment grow" --> Reactive systems
* <!-- .element: class="fragment grow" --> Eclipse Vert.x

---

## Blockchain?

<span class="fragment">the new ducktape?</span>
<span class="fragment">a database?</span>
<span class="fragment">money?</span>
<span class="fragment">the sollution to all your problems?</span>
<span class="fragment">a miracle?</span>

---

<!-- .slide: data-background="images/secret.gif" data-background-size="contain" data-background-video-loop="true" -->

## Nope!

---

## A Linked List!

```java
class Block {
    Object data;
    Block previous;
}
```

---

## Sort of...

```java
class Block {
  int index;
  long timestamp;
  Object data;
  int nonce;
  String previousHash;
}
```

---

<!-- .slide: data-background-video="images/job-done.mp4" data-background-size="contain" data-background-video-loop="true" -->
## The End

---

## Not so fast!

* **It's a glorified linked list**
* <!-- .element: class="fragment grow" --> That works on top of a P2P network

---

<!-- .slide: data-background-video="images/challenge-accepted.mp4" data-background-size="contain" data-background-video-loop="true" -->

## Challenge!

#### Let's build one ourselves!

---

## I need...

* One **idea**...
* A **`P2P`** network
* A **`Linked List`** on steroids

---

<!-- .slide: data-background-video="images/idea.mp4" data-background-size="contain" data-background-video-loop="true" -->
## One Idea...

---

<!-- .slide: data-background-video="images/lazzy.mp4" data-background-size="contain" data-background-video-loop="true" -->

## P2P Network...

---

* Will use Eclipse Vert.x
* <!-- .element: class="fragment grow" --> It's a **Reactive System**
* <!-- .element: class="fragment grow" --> It's **polyglot**
* <!-- .element: class="fragment grow" --> The Eventbus lets me connect anything together

---

<!-- .slide: data-background-video="images/hacking.mp4" data-background-size="contain" data-background-video-loop="true" -->

## Hacking it all together

---

## Block

```java
public class Block {
  private int index;
  private long timestamp;
  private String data;
  private int nonce;
  private String previousHash;
}
```

---

## BlockChain

```java
public interface Blockchain {
  // P2P magic!!!
  Blockchain start(Handler handler);
  Blockchain stop(Handler handler);
  // Lookup
  int size();
  Block get(int index);
  Block last();
  // event handlers
  Blockchain blockHandler(Handler handler);
  Blockchain replaceHandler(Handler handler);
  // forge a new block
  Blockchain add(String data, Handler handler);
}
```

---

## Implementation

```java
// the internal state
chain = new ArrayList<>();
// insert the genesis block
chain.add(new Block()
  .setIndex(0)
  .setNonce(1)
  .setPreviousHash("")
  .setData("<genesis>"));
```

---

## P2P

```java
// Announce we're alive!
eb.send(address, {}, (err, res) -> {
  consensus(res)
    .stream()
    .map(json -> new Block(json))
    .collect(Collectors.toList()));
});
```

---

## P2P

```java
// start listening for events
messageConsumer = eb.consumer(address, message -> {
  if (message != null) {
    // if there is a body it is a mine event
    consensus(Collections.singletonList(new Block(message)));
  } else {
    // when there is no message it's a chain request
    JsonArray json = new JsonArray();
    for (Block block : chain) {
      json.add(block.toJson());
    }

    message.reply(json);
  }
});
```

---

<!-- .slide: data-background-video="images/consensus.mp4" data-background-size="contain" data-background-video-loop="true" -->

## Consensus?

---

## Algorithm

```java
/**
 * This is our consensus algorithm, it resolves conflicts
 * by replacing our chain with the longest one in the network.
 */
public void consensus(List<Block> receivedBlocks) {
  // for brevity and simplicity, the longest valid chain
  //  wins and replaces the existing one
  // not super smart but it's a good start
  ...
  // Peer store is longer than current store.
  if (validChain(receivedBlocks)) {
    chain.clear();
    chain.addAll(receivedBlocks);
  }
}
```

---

<!-- .slide: data-background-video="images/pow.mp4" data-background-size="contain" data-background-video-loop="true" -->

## Proof of Work?

---

## Algorithm

```java
/**
 * Simple Proof of Work Algorithm:
 * - Find a number p' such that hash(pp') contains
 *   leading 4 zeroes, where p is the previous p'
 * - p is the previous proof, and p' is the new proof
 */
public int proofOfWork(int lastProof) {
  int proof = 0;
  while (!validProof(lastProof, proof)) {
    proof++;
  }

  return proof;
}
```

---

## DEMO

---

<!-- .slide: data-background-video="images/blockchain.mp4" data-background-size="contain" -->

---

<!-- .slide: data-background-video="images/perfect.mp4" data-background-size="contain" data-background-video-loop="true" -->

## Remember last December?

---

* <!-- .element: class="fragment grow" --> Thread oriented apps **can't scale**
* <!-- .element: class="fragment grow" --> **Self inflicted DDoS**
* <!-- .element: class="fragment grow" --> Not using a **Reactive System**

---

### Quick recap on Threading

* <!-- .element: class="fragment grow" --> has it's own **memory**
* <!-- .element: class="fragment grow" --> is **managed** by the OS or VM
* <!-- .element: class="fragment grow" --> suffers from **contention** and **locks**
* <!-- .element: class="fragment grow" --> **requires** extra logic (scheduling)

---

### Example

* add a value to a queue
* <!-- .element: class="fragment" --> execution time on same thread: **0.01&#181;s**

---

### Threaded Example

* add a value to a queue (on a thread)
* <!-- .element: class="fragment" --> execution time on a **new** thread: **~71.3&#181;s**
* <!-- .element: class="fragment" --> imagine **10k** users: **~0.7s** (just for threading)
* <!-- .element: class="fragment" --> now think that we do **more than** just 1 op in a app...
* <!-- .element: class="fragment" --> the waiting starts the **snow ball effect**...

---

<!-- .slide: data-background-video="images/sucks.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

### Ethereum: Web3J

* <!-- .element: class="fragment grow" --> web3j is a **lightweight**
* <!-- .element: class="fragment grow" --> **reactive** <small class="fragment">*cough!* *cough!*</small>
* <!-- .element: class="fragment grow" --> **type safe** Java and Android

---

<!-- .slide: data-background-video="images/confident.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

```java
...
public class Async {
  ...
  private static final ExecutorService executor =
    Executors.newCachedThreadPool();
}
```
<small>[core/src/main/java/org/web3j/utils/Async.java](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/utils/Async.java)</small>

---

```java
...
  private static int getCpuCount() {
    return Runtime.getRuntime().availableProcessors();
  }
...
```
<small>[core/src/main/java/org/web3j/utils/Async.java](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/utils/Async.java)</small>

---

```java
public static <T> CompletableFuture<T> run(Callable<T> callable) {
  CompletableFuture<T> result = new CompletableFuture<>();
  CompletableFuture.runAsync(() -> {
    // we need to explicitly catch any exceptions,
    // otherwise they will be silently discarded
    try {
      result.complete(callable.call());
    } catch (Throwable e) {
      result.completeExceptionally(e);
    }
  }, executor);
  return result;
}
```
<small>[core/src/main/java/org/web3j/utils/Async.java](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/utils/Async.java)</small>

---

<!-- .slide: data-background-video="images/wtf.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

<!-- .slide: style="text-align: left;" -->
# Thank you!

* https://www.jetdrone.xyz
* https://twitter.com/pml0pes
* https://github.com/vert-x3
* https://www.reactivemanifesto.org
* https://github.com/pmlopes/presentation
