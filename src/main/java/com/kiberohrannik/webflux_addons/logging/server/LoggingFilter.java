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
public class LoggingFilter implements WebFilter {

    private static final Log log = LogFactory.getLog(LoggingFilter.class);
    private final ServerMessageCreator serverMessageCreator;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return serverMessageCreator.formatMessage(exchange.getRequest())
                .doOnNext(log::info)
                .then(chain.filter(exchange));
    }
}