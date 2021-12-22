package com.kiberohrannik.webflux_addons.logging.server;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
public class TempController {

    @PostMapping("/test/endpoint")
    public Mono<String> testEndpoint(@RequestBody Mono<String> requestBody, ServerWebExchange exchange) {
        exchange.getResponse().setRawStatusCode(220);
        return requestBody
                .switchIfEmpty(Mono.error(() -> {
                    return new RuntimeException();
                }))
                .doOnNext(Objects::requireNonNull)
                .map(body -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return body.concat("-RESPONSE");
                });
    }
}
