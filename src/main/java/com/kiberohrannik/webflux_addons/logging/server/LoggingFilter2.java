package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.server.message.ServerMessageCreator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class LoggingFilter2 implements WebFilter {

    private static final Log log = LogFactory.getLog(LoggingFilter2.class);
    private final ServerMessageCreator serverMessageCreator;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<Void> mono = chain.filter(exchange);

        exchange.getResponse().beforeCommit(
                () -> Mono.defer(() -> Mono.just(exchange))
                        .doOnNext(exch -> log.info(serverMessageCreator.createForResponse(exch)))
                        .then());

        return mono;
    }
}