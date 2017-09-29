package spring5;

import math.Pi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.lang.management.ManagementFactory;

@RestController
public class PiController {

    private static final String PID =
            ManagementFactory.getRuntimeMXBean().getName();

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("Current PID: ")
                .map(w -> w + PID);
    }

    @GetMapping("/work")
    public Mono<String> work() {
        return Mono.just("Pi is: ")
                .map(w -> w + Pi.calculate(100_000_000));
    }
}