package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.RequestData;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ServerMessageFormatter {

//    Mono<String> addData(ServerWebExchange exchange, LoggingProperties loggingProperties, Mono<String> sourceMessage);
    Mono<RequestData> addData(LoggingProperties loggingProperties, Mono<RequestData> sourceMessage);

}
