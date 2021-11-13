package com.kiberohrannik.webflux_addons.old;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.lang.reflect.Field;

@Log4j2
public final class ClientRequestBodyExtractor {

    public Object extractBody(@NonNull ClientRequest request) {
        BodyInserter<?, ? super ClientHttpRequest> inserter = request.body();

        Field bodyField;
        try {
            bodyField = inserter.getClass().getDeclaredField("arg$1");
        } catch (NoSuchFieldException e) {
            return null;
        }

        try {
            bodyField.setAccessible(true);

            Object bodyValue = bodyField.get(inserter);
            bodyField.setAccessible(false);

            return bodyValue;

        } catch (IllegalAccessException e) {
            bodyField.setAccessible(false);
            log.error(e.getMessage());

            return null;
        }
    }
}
