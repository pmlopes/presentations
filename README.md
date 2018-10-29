<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

### Building native-images for fun and profit!

<p>&nbsp;</p>
`Paulo Lopes - @pml0pes` <!-- .element style="font-size: 0.5em" -->

Notes:

---
<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

### Hello

```java
public class Hello {
  public static void main(String[] args) {
    System.out.println("Hi, I'm Paulo!");
  }
}
```
<pre><code data-trim data-noescape><small>
$ <span class="fragment">javac Hello.java
$ </span><span class="fragment">native-image Hello</span>
<span class="fragment">Build on Server(pid: 13575, port: 44807)*
[hello:13575]    classlist:   1,317.64 ms
[hello:13575]        (cap):   1,159.12 ms
[hello:13575]        setup:   2,575.85 ms
[hello:13575]   (typeflow):   4,667.41 ms
[hello:13575]    (objects):   2,459.68 ms
[hello:13575]   (features):     107.04 ms
[hello:13575]     analysis:   7,370.71 ms
[hello:13575]     universe:     419.05 ms
[hello:13575]      (parse):     837.78 ms
[hello:13575]     (inline):   1,295.41 ms
[hello:13575]    (compile):   6,893.52 ms
[hello:13575]      compile:   9,470.76 ms
[hello:13575]        image:   1,003.65 ms
[hello:13575]        write:     228.39 ms
[hello:13575]      [total]:  22,461.86 ms</span>
</small></code></pre>

---
<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

### Time

<div style="display: inline-block; width: 48%; float: left;">
<pre style="width: 100%"><code data-trim class="sh">
$ time java Hello
Hi, I'm Paulo!

real    0m0,085s
user    0m0,063s
sys     0m0,026s
</code></pre></div>

<div style="display: inline-block; width: 48%;" class="fragment">
<pre style="width: 100%"><code data-trim class="java">
$ time ./hello
Hi, I'm Paulo!

real    0m0,004s
user    0m0,002s
sys     0m0,003s
</code></pre></div>

## 20x speedup!!! <!-- .element class="fragment" -->

---
<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

### Super fast!

![ferrari](media/ferrari.jpg) <!-- .element class="stretch" -->

Notes:
  Given the previous result, many will assume that native images are super fast. Before going to that direction, let's see what the team behind the project say are the real use cases.

---
<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

## Use cases

---
<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

### Native Images

* Startup time matters
  * Short-running command line applications
  * Serverless cloud functions
* Memory footprint matters
  * Small to medium-sized heaps (100Mb ~ 1Gb)
* All java code is known ahead of time
  * Single-application cloud server

---
<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

### Regular Java VM

* Heaps size is large
  * Big Data analytics
  * Multiple GByte ~ TByte heap size
* Classes are only known at run time
  * "traditional" java application server

---
<!-- .slide: data-background-image="media/rht-bg.png" data-background-size="contain" -->

## Serverless
#### (Demo)

Notes:
  faas template pull https://github.com/pmlopes/openfaas-svm-vertx
  faas new --lang vertx-svm hello-jfall

---

