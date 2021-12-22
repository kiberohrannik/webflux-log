package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.server.message.ServerMessageCreator;
import com.kiberohrannik.webflux_addons.logging.server.message.LoggingServerHttpRequestDecorator;
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
        long startMillis = System.currentTimeMillis();

        String logMessage = serverMessageCreator.createForRequest(exchange);

        LoggingServerHttpRequestDecorator loggingDecorator
                = new LoggingServerHttpRequestDecorator(exchange.getRequest(), logMessage);

        return chain.filter(exchange.mutate().request(loggingDecorator).build())
                .doFinally(signalType -> {
//                    log.info("Elapsed Time: " + (System.currentTimeMillis() - startMillis) + " ms");
                    log.info(serverMessageCreator.createForResponse(exchange, System.currentTimeMillis() - startMillis));
                });
    }
}