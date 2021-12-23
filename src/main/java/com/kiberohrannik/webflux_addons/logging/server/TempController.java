package com.kiberohrannik.webflux_addons.logging.server;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Random;

@RestController
public class TempController {

    @PostMapping("/test/endpoint")
    public Mono<String> testEndpoint(@RequestBody Mono<String> requestBody) {
        return requestBody
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .doOnNext(Objects::requireNonNull)
                .map(body -> {
                    try {
                        int delay = new Random().nextInt(300);
                        System.out.println("delay = " + delay);
                        Thread.sleep(delay);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return body.concat("-RESPONSE");
                });
    }
}
