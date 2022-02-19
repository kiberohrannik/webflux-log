package com.kv.webflux.logging.client.request.message.formatter.extractor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

public final class RequestBodyExtractor {

    private static final Log log = LogFactory.getLog(RequestBodyExtractor.class);

    private static final String BODY_VALUE_FIELD = "arg$1";
    private static final String BODY_TYPE_FIELD = "arg$2";

    private final RequestBodyMapper bodyMapper = new RequestBodyMapper();


    public Mono<String> extractBody(@NonNull ClientRequest request) {
        BodyInserter<?, ? super ClientHttpRequest> inserter = request.body();

        Object bodyValue = getInserterFieldValue(inserter, BODY_VALUE_FIELD);

        if (bodyValue == null) {
            log.debug("Empty body in request: " + request.url());
            return Mono.empty();
        }

        Object bodyType = getInserterFieldValue(inserter, BODY_TYPE_FIELD);

        return bodyMapper.mapToString(bodyValue, bodyType);
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
            log.error("Failed to extract request body: " + e.getMessage());

            return null;
        }
    }
}
