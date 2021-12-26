package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.server.message.logger.ServerRequestLogger;
import com.kiberohrannik.webflux_addons.logging.server.message.logger.ServerResponseLogger;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class LoggingFilter implements WebFilter {

    private final ServerRequestLogger requestMessageCreator;
    private final ServerResponseLogger responseMessageCreator;


    LoggingFilter(ServerRequestLogger requestMessageCreator,
                  ServerResponseLogger responseMessageCreator) {

        this.requestMessageCreator = requestMessageCreator;
        this.responseMessageCreator = responseMessageCreator;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startMillis = System.currentTimeMillis();

        ServerHttpRequest loggedRequest = requestMessageCreator.log(exchange);
        ServerHttpResponse loggedResponse = responseMessageCreator.log(exchange, startMillis);

        return chain.filter(exchange.mutate().request(loggedRequest).response(loggedResponse).build());
    }
}