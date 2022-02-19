package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import com.kv.webflux.logging.client.response.message.ResponseData;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

public class BodyClientResponseFormatter {

    public Mono<ResponseData> formatMessage(ClientResponse response, LoggingProperties properties) {
        return properties.isLogBody()
                ? formatMessage(response)
                : Mono.just(new ResponseData(response, LoggingUtils.EMPTY_MESSAGE));
    }


    private Mono<ResponseData> formatMessage(ClientResponse response) {
        return response.bodyToMono(DataBuffer.class)
                .map(body -> {
                    ClientResponse cloned = response.mutate().body(Flux.just(body)).build();
                    return new ResponseData(cloned, formatMessage(body.toString(Charset.defaultCharset())));
                })
                .defaultIfEmpty(new ResponseData(response, formatMessage(LoggingUtils.NO_BODY_MESSAGE)));
    }

    private String formatMessage(String body) {
        return " BODY: [ ".concat(body).concat(" ]");
    }
}