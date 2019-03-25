<!-- .slide: style="text-align: left;" -->
# Holly GraalVM
#### Java native-mages!

<div style="font-size: 0.6em">
  **Paulo Lopes**<br/>
  <i class="fab fa-redhat"></i> Principal Software Engineer
</div>

---

# January 1996

*write once, run everywhere*

---

| Release | Date     | Features     |
| ------- |:---------|-------------:|
| 1.0     | Jan 1996 | <span class="hl-purple">**Interpreted**</span> |
| 1.1     | Feb 1997 | <span class="hl-purple">**Interpreted**</span> |
| 1.2     | Dec 1998 | JIT |

---

# JIT

---

1. given a "`.class`" file
2. the code is loaded into memory         <!-- .element: class="fragment" -->
3. is verified for bytecode errors        <!-- .element: class="fragment" -->
4. the interpreter runs the program       <!-- .element: class="fragment" -->
5. the JIT compiles to CPU's native ISA   <!-- .element: class="fragment" -->
6. code is replaced                       <!-- .element: class="fragment" -->

---

```java
public class Hello {
  public static void main(String[] args) {
    System.out.println("Hello Java!");
  }
}
```

---

```
$ javac Hello.java
$ perf stat -e instructions java Hello
Hello Java!

 Performance counter stats for 'java Hello':

       172,099,420      instructions:u

       0.046144981 seconds time elapsed

       0.041163000 seconds user
       0.011130000 seconds sys
```

---
<!-- .slide: style="text-align: left;" -->

## Time: <span class="fragment">0.041163000<small>s</small></span>
## Assembly: <span class="fragment">172,099,420</span>

---

# Back to 1998

---
<!-- .slide: style="text-align: left;" -->

### GNU Compiler for Java

> *GCJ compiles Java source code to Java Virtual Machine bytecode or to machine code for a number of CPU architectures. <small>Wikipedia</small>*

---

```
$ docker run --rm -it ubuntu:trusty /bin/bash
# apt-get update && \
  apt-get install gcj-4.8 gcj-jdk
# # compile java (1.4) code
# gcj-4.8 -c -g -O Hello.java
# # link like a C++ app
# gcj --main=Hello -o Hello Hello.o
# file Hello
Hello: ELF 64-bit LSB  executable, x86-64, version 1 (SYSV),
 dynamically linked (uses shared libs), for GNU/Linux 2.6.24,
 BuildID[sha1]=febbed6d6162f637a7c3b9203a87969cc6a5f2b2,
 not stripped
```

---

## GCJ

* Great starup time
* Performance not that much
* required all sources to be available
* custom runtime (`libgcj`)

---

```
# ldd Hello
	linux-vdso.so.1 =>  (0x00007ffd6ebd7000)
	libgcj.so.14 => /usr/lib/x86_64-linux-gnu/libgcj.so.14
	libc.so.6 => /lib/x86_64-linux-gnu/libc.so.6
	libpthread.so.0 => /lib/x86_64-linux-gnu/libpthread.so.0
	librt.so.1 => /lib/x86_64-linux-gnu/librt.so.1
	libdl.so.2 => /lib/x86_64-linux-gnu/libdl.so.2
	libz.so.1 => /lib/x86_64-linux-gnu/libz.so.1
	/lib64/ld-linux-x86-64.so.2
	libgcc_s.so.1 => /lib/x86_64-linux-gnu/libgcc_s.so.1
```

---

<div style="display: inline-block; width: 50%; float: left;">
<pre><code data-trim data-noescape>
Using Valgrind-3.10.1
Command: java Hello

Events    : Ir
Collected : 208271295

I   refs:      208,271,295

# time java Hello
Hello Java!

real  0m0.110s
user  0m0.082s
sys   0m0.023s
</code></pre>
</div>
<div style="display: inline-block; width: 50%;">
<pre><code data-trim data-noescape>
Using Valgrind-3.10.1
Command: ./Hello

Events    : Ir
Collected : 41615107

I   refs:      41,615,107

# time ./Hello
Hello Java!

real  0m0.045s
user  0m0.021s
sys   0m0.024s
</code></pre>
</div>

---
<!-- .slide: style="text-align: left;" -->

### Flash forward
# 2018

---
<!-- .slide: style="text-align: left;" -->

## GraalVM

* JIT                       <!-- .element: class="fragment" -->
* Polyglot frontend         <!-- .element: class="fragment" -->
* Native image generator    <!-- .element: class="fragment" -->

---
<!-- .slide: style="text-align: left;" -->

## SubstrateVM

*Substrate VM is a framework that allows **ahead-of-time** compilation of Java applications under **closed-world** assumption into executable images or shared objects.*

---

```java
public class Static {
    private static String message;

    static {
      Thread.sleep(1000);
      message = "Hello from " + new Date();
      System.out.println("Initialized!");
    }

    public static void main(String[] args) {
      System.out.println(message);
    }
}
```

---

<pre><code data-trim data-noescape>
$ javac Static.java
$ native-image Static
Build on Server(pid: 28926, port: 44667)
...
<mark>Initialized!</mark>
...
[static:28926]      compile:   4,939.63 ms
[static:28926]        image:     557.42 ms
[static:28926]        write:     112.96 ms
[static:28926]      [total]:  14,481.05 ms
</code></pre>

---
<!-- .slide: style="text-align: left;" -->

## Why is SVM fast?

* All classes are known at compile <small>(no dynamic code)</small>   <!-- .element: class="fragment" -->
* Static initializers are run at compile                              <!-- .element: class="fragment" -->
* Initial memory is pre-loaded                                        <!-- .element: class="fragment" -->

---

<div style="display: inline-block; width: 50%; float: left;">

<pre><code data-trim data-noescape>
$ time java Static
Initialized!
Hello from Tue Mar 26 2019

real    0m1.111s
user    0m0.113s
sys     0m0.021s
</code></pre>

</div>
<div style="display: inline-block; width: 50%;" class="fragment">

<pre><code data-trim data-noescape>
$ time ./static
Hello from Tue Nov 27 2018

real    0m0.014s
user    0m0.003s
sys     0m0.011s
</code></pre>

</div>

---

<pre><code data-trim data-noescape>
$ time ./static
Hello from <mark>Tue Nov 27 2018</mark>

real    0m0.014s
user    0m0.003s
sys     0m0.011s
</code></pre>

---
<!-- .slide: style="text-align: left;" -->

# Caveats

---

```java
@RestController
public class HelloController {
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
```

---

```
$ mvn clean package
...
$ native-image -jar target/demo-0.0.1-SNAPSHOT.jar \
  --report-unsupported-elements-at-runtime

Build on Server(pid: 25469, port: 39497)
...
[demo-0.0.1-SNAPSHOT:25469]      compile:  13,417.57 ms
[demo-0.0.1-SNAPSHOT:25469]        image:   1,073.00 ms
[demo-0.0.1-SNAPSHOT:25469]        write:     254.86 ms
[demo-0.0.1-SNAPSHOT:25469]      [total]:  25,654.61 ms
```

---

<pre><code data-trim data-noescape>
<small>
$ ./demo-0.0.1-SNAPSHOT
Exception in thread "main" java.lang.IllegalStateException: java.lang.IllegalStateException:
                                                            Unable to determine code source archive
        at org.springframework.boot.loader.ExecutableArchiveLauncher.<init>(ExecutableArchiveLauncher.java:41)
        at org.springframework.boot.loader.JarLauncher.<init>(JarLauncher.java:35)
        at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:51)
Caused by: java.lang.IllegalStateException: Unable to determine code source archive
        at org.springframework.boot.loader.Launcher.createArchive(Launcher.java:122)
        at org.springframework.boot.loader.ExecutableArchiveLauncher.<init>(ExecutableArchiveLauncher.java:38)
        ... 2 more
</small>
</code></pre>

---
<!-- .slide: data-background-image="media/red-pill-blue-pill.jpg" -->

---
<!-- .slide: style="text-align: left;" -->

# Thank you!

* https://vertx-starter.jetdrone.xyz
* https://github.com/eclipse-vertx/vert.x
* https://github.com/oracle/graal

---
<!-- .slide: style="text-align: left;" -->

# Native Image
### Workshop

---

> give a man a fish and you feed him for a day; teach a man to fish and you feed him for a lifetime

---
<!-- .slide: style="text-align: left;" -->

# Step #1

[source code](./steps/step-1)

Notes:
  Start with a simple vert.x application.
  mvn clean package
  mvn vertx:run ...

---
<!-- .slide: style="text-align: left;" -->

# Step #2

[source code](./steps/step-2)

Notes:
  Add the graal plugin
  Since project dependencies include optional dependencies we must signal that the class path is incomplete

---
<!-- .slide: style="text-align: left;" -->

# Step #3

[source code](./steps/step-3)

Notes:
  Solve the closed world assumption
  Add a main
  Tell graal to rerun the initialization at runtime to special classes that have state (solve the date problem, remember)

---
<!-- .slide: style="text-align: left;" -->

# Step #4

[source code](./steps/step-4)

Notes:
  So libraries use native code a reference memory during initialization, this is not valid at runtime as the memory space
  is different, so we must tell graal to recompute that address.

---
<!-- .slide: style="text-align: left;" -->

# Step #5

[source code](./steps/step-5)

Notes:
  Security requires certificates in keystore format but it also needs more

---
<!-- .slide: style="text-align: left;" -->

# Step #6

[source code](./steps/step-6)

Notes:
  Security requires an extra flag (which will increase the binary) and a hard dependency on libsunec.so

---
<!-- .slide: style="text-align: left;" -->

# Step #7

[source code](./steps/step-7)

Notes:
  Clients can use security (SSL) too but
  ./hello_native -Dvertx.disableDnsResolver=true
  This is annoying so you can patch library code using a substitution

---
<!-- .slide: style="text-align: left;" -->

# Step #8

[source code](./steps/step-8)

Notes:
  Reflection can be used to, for example lets build all 3 verticles in the same elf
  You need to tell all reflection usage to graal compiler, you can do it manuall or use the agent

---
<!-- .slide: style="text-align: left;" -->

# Step #9

[source code](./steps/step-9)

Notes:
  The default verticle is on the MANIFEST so the elf needs to know about which resources to bundle

---
<!-- .slide: data-background-image="media/kung-fu.jpg" -->

---
<!-- .slide: style="text-align: left;" -->

# Step #10
### Bonus

[source code](./steps/step-10)

Notes:
  mvn io.quarkus:quarkus-maven-plugin:0.12.0:create \
    -DprojectGroupId=org.acme \
    -DprojectArtifactId=getting-started \
    -DclassName="org.acme.quickstart.GreetingResource" \
    -Dpath="/hello"

  mvn -Pnative clean package
