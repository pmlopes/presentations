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

1. <!-- .element: class="fragment grow" --> Bockchain (the cool parts)
2. <!-- .element: class="fragment grow" --> Eclipse Vert.x
3. <!-- .element: class="fragment grow" --> Reactive Blockchain FTW!

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

## Nope!

---

<!-- .slide: data-background="images/secret.gif" data-background-size="contain" data-background-video-loop="true" -->

---

## Sounds like a Linked List!

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

---

## Not so fast!

* **It's a glorified linked list**
* <!-- .element: class="fragment grow" --> That works on top of a P2P network

---

## Challenge!

#### Let's build one ourselves!

---

<!-- .slide: data-background-video="images/challenge-accepted.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

## Ingredients

* One idea...
* A `P2P` network
* A `Linked List` on steroids


---

## One Idea...

---

<!-- .slide: data-background-video="images/idea.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

## P2P Network...

---

<!-- .slide: data-background-video="images/lazzy.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

* <!-- .element: class="fragment grow" --> Will use Eclipse Vert.x
* <!-- .element: class="fragment grow" --> It's a **Reactive System**
* <!-- .element: class="fragment grow" --> It's **polyglot**
* <!-- .element: class="fragment grow" --> The Eventbus lets me connect anything together

---

## Hacking it all together

---

<!-- .slide: data-background-video="images/hacking.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

## DEMO

---

<!-- .slide: data-background-video="images/blockchain.mp4" data-background-size="contain" -->

---

## RANTS!

* Why did exchanges went down last December?

---

* <!-- .element: class="fragment grow" --> Thread oriented apps can't scale
* <!-- .element: class="fragment grow" --> Self inflicted DDoS
* <!-- .element: class="fragment grow" --> Not using a Reactive System

---

### Quick recap on Threading

* has it's own memory
* is managed by the OS or VM
* Has contention and locks
* Requires extra logic (scheduling)

---

### Example

* add a value to a queue
* execution time on same thread: 0.01&#181;s

---

### Threaded Example

* add a value to a queue (on a thread)
* <!-- .element: class="fragment" --> execution time on a single thread: **~71.3&#181;s**
* <!-- .element: class="fragment" --> imagine **10k** users: **~0.7s** (just for threading)
* <!-- .element: class="fragment" --> now think that we do more than just 1 op in a app...
* <!-- .element: class="fragment" --> the waiting starts to build exponentially...

---

<!-- .slide: data-background-video="images/sucks.mp4" data-background-size="contain" data-background-video-loop="true" -->

---

### I'm cool I'm a Web3J user

* web3j is a lightweight
* reactive
* type safe Java and Android

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
