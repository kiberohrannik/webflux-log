package com.kiberohrannik.webflux_addons.logging.creator;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

public class RequestBodyMapper {

    public Mono<String> mapToString(Object body, Object bodyType) {
        if (body instanceof String) {
            return Mono.just(body.toString());
        }

        if (body instanceof Publisher) {
            return Mono.from((Publisher<?>) body).map(Object::toString);
        }

        if (bodyType instanceof ParameterizedTypeReference || bodyType instanceof Class) {
            return Mono.just(body.toString());
        }

        return Mono.empty();
    }
}