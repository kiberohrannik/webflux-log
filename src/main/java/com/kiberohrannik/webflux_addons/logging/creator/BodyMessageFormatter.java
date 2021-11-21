package com.kiberohrannik.webflux_addons.logging.creator;

import com.kiberohrannik.webflux_addons.logging.filter.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class BodyMessageFormatter implements RequestDataMessageFormatter {

    private final RequestBodyExtractor bodyExtractor = new RequestBodyExtractor();


    @Override
    public Mono<String> addData(ClientRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogBody()) {
            return sourceMessage.flatMap(source -> addBody(request, source));
        }

        return sourceMessage;
    }


    private Mono<String> addBody(ClientRequest request, String source) {
        return bodyExtractor.extractBody(request)
                .switchIfEmpty(Mono.just(""))
                .map(bodyStr -> source.concat("\nBODY: [ ").concat(bodyStr).concat(" ]"));
    }
}