package com.kiberohrannik.webflux_addons.logging.creator;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.ClassUtils;
import reactor.core.publisher.Mono;

public class RequestBodyMapper {

    public Mono<String> mapToString(Object body) {
        if (body instanceof String) {
            return Mono.just(body.toString());
        }

        if (body instanceof Publisher) {
            return Mono.from((Publisher<?>) body).map(Object::toString);
        }

        return Mono.empty();
    }

    public <T> Mono<String> mapToString(Object body, Class<T> type) {
        if (ClassUtils.isAssignable(body.getClass(), type)) {
            return Mono.just(body.toString());
        }

        if (body instanceof Publisher) {
            return Mono.from((Publisher<?>) body).map(Object::toString);
        }

        return Mono.empty();
    }

    public Mono<String> mapToString(Object body, ParameterizedTypeReference<?> type) {
        if (ClassUtils.isAssignable(body.getClass(), type.getClass())) {
            return Mono.just(body.toString());
        }

        if (body instanceof Publisher) {
            return Mono.from((Publisher<?>) body).map(Object::toString);
        }

        return Mono.empty();
    }
}