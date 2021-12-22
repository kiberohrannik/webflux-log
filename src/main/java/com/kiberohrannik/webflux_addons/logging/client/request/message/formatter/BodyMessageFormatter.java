package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.client.request.message.formatter.extractor.RequestBodyExtractor;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class BodyMessageFormatter implements RequestDataMessageFormatter {

    private final RequestBodyExtractor bodyExtractor = new RequestBodyExtractor();


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties logProps, Mono<String> source) {
        return logProps.isLogBody()
                ? source.flatMap(message -> addBody(request, message))
                : source;
    }


    private Mono<String> addBody(ClientRequest request, String source) {
        return bodyExtractor.extractBody(request)
                .switchIfEmpty(Mono.just(LoggingUtils.NO_BODY_MESSAGE))
                .map(bodyStr -> source.concat("\nBODY: [ ").concat(bodyStr).concat(" ]"));
    }
}