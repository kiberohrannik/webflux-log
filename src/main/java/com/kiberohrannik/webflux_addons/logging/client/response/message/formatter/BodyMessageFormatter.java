package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class BodyMessageFormatter implements ResponseDataMessageFormatter {

    @Override
    public Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source) {
        if (logProps.isLogBody()) {
            return source.flatMap(message -> addBody(message.getResponse(), message.getLogMessage()));
        }

        return source;
    }


    private Mono<ResponseData> addBody(ClientResponse response, String source) {
        return response.bodyToMono(String.class)
                .map(body -> {
                    ClientResponse cloned = response.mutate().body(body).build();
                    return new ResponseData(cloned, formatMessage(body, source));
                })
                .switchIfEmpty(
                        Mono.just(new ResponseData(response, formatMessage(LoggingUtils.NO_BODY_MESSAGE, source))));
    }

    private String formatMessage(String body, String source) {
        return source.concat("\nBODY: [ ").concat(body).concat(" ]");
    }
}