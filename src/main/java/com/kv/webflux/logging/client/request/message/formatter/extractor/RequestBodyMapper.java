package com.kv.webflux.logging.client.request.message.formatter.extractor;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

class RequestBodyMapper {

    public Mono<String> mapToString(Object body, Object bodyType) {
        if (body instanceof Publisher) {
            return Mono.from((Publisher<?>) body).map(Object::toString);
        }

        if (body instanceof String || isExplicitBodyType(bodyType)) {
            return Mono.just(body.toString());
        }

        return Mono.empty();
    }


    private boolean isExplicitBodyType(Object bodyType) {
        return bodyType instanceof ParameterizedTypeReference || bodyType instanceof Class;
    }
}