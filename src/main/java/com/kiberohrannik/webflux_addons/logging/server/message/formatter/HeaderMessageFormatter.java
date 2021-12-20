package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.extractor.HeaderExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class HeaderMessageFormatter implements ServerMessageFormatter {

    private final HeaderExtractor headerExtractor;


    @Override
    public Mono<String> addData(ServerHttpRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogHeaders()) {
            return sourceMessage.map(source ->
                    source.concat(headerExtractor.extractHeaders(request.getHeaders(), loggingProperties)));
        }

        return sourceMessage;
    }
}