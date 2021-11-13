package com.kiberohrannik.webflux_addons.logging.creator;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

@Log4j2
final class RequestBodyExtractor {

    private final RequestBodyMapper bodyMapper = new RequestBodyMapper();


    public Mono<String> extractBody(@NonNull ClientRequest request) {
        BodyInserter<?, ? super ClientHttpRequest> inserter = request.body();

        Object bodyValue = getInserterFieldValue(inserter, "arg$1");

        if (bodyValue == null) {
            return Mono.empty();
        }

        Object bodyType = getInserterFieldValue(inserter, "arg$2");

        if (bodyType == null) {
            return bodyMapper.mapToString(bodyValue);
        }

        if (bodyType instanceof ParameterizedTypeReference) {
            return bodyMapper.mapToString(bodyValue, (ParameterizedTypeReference<?>) bodyType);
        }

        return bodyMapper.mapToString(bodyValue, (Class<?>) bodyType);
    }


    private Object getInserterFieldValue(BodyInserter<?, ? super ClientHttpRequest> inserter, String fieldName) {
        Field inserterField;
        try {
            inserterField = inserter.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }

        try {
            inserterField.setAccessible(true);

            Object bodyType = inserterField.get(inserter);
            inserterField.setAccessible(false);

            return bodyType;

        } catch (IllegalAccessException e) {
            inserterField.setAccessible(false);
            log.error(e.getMessage());

            return null;
        }
    }
}
