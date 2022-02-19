package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import com.kv.webflux.logging.client.request.message.formatter.extractor.RequestBodyExtractor;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class BodyMessageFormatter implements RequestDataMessageFormatter {

    private final RequestBodyExtractor bodyExtractor = new RequestBodyExtractor();


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties properties) {
        return properties.isLogBody()
                ? addBody(request)
                : Mono.just(LoggingUtils.EMPTY_MESSAGE);
    }


    private Mono<String> addBody(ClientRequest request) {
        return bodyExtractor.extractBody(request)
                .defaultIfEmpty(LoggingUtils.NO_BODY_MESSAGE)
                .map(bodyStr -> " BODY: [ ".concat(bodyStr).concat(" ]"));
    }
}