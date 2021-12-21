package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public final class BodyMessageFormatter implements ServerMessageFormatter {

    @Override
    public Mono<String> addData(ServerWebExchange exchange,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogBody()) {
            return sourceMessage.flatMap(source -> addBody(exchange.getRequest(), source));
        }

        return sourceMessage;
    }


    private Mono<String> addBody(ServerHttpRequest request, String source) {
        return request.getBody()
                .singleOrEmpty()

                .map(CachedDataBuffer::new)
                .map(CachedDataBuffer::getCachedContent)

                .switchIfEmpty(Mono.just(LoggingUtils.NO_BODY_MESSAGE))
                .map(bodyStr -> createMessage(source, bodyStr));
    }


    private String createMessage(String source, String bodyStr) {
        return source.concat(" BODY: [ ").concat(bodyStr).concat(" ]");
    }
}