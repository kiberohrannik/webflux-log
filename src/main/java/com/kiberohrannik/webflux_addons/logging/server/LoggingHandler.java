package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.server.message.ServerMessageCreator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoggingHandler implements HttpHandler {

    private static final Log log = LogFactory.getLog(LoggingHandler.class);


    @Override
    public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
        return Mono.just("").doOnNext(s -> log.info("=================vfavfqvbrf")).then();
    }
}