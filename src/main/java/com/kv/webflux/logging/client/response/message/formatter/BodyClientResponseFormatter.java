package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import com.kv.webflux.logging.client.response.message.ResponseData;
import com.kv.webflux.logging.provider.BodyProvider;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BodyClientResponseFormatter {

    private final BodyProvider provider = new BodyProvider();


    public Mono<ResponseData> formatMessage(ClientResponse response, LoggingProperties properties) {
        return properties.isLogBody()
                ? formatMessage(response)
                : Mono.just(new ResponseData(response, LoggingUtils.EMPTY_MESSAGE));
    }


    private Mono<ResponseData> formatMessage(ClientResponse response) {
        return response.bodyToMono(DataBuffer.class)
                .map(body -> {
                    ClientResponse cloned = response.mutate()
                            .body(Flux.just(body))
                            .build();

                    return new ResponseData(cloned, provider.createBodyMessage(body));
                })
                .defaultIfEmpty(new ResponseData(response, provider.createNoBodyMessage()));
    }
}