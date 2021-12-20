package com.kiberohrannik.webflux_addons.logging.server.message;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface ServerMessageCreator {

    Mono<String> formatMessage(ServerHttpRequest request);
}
