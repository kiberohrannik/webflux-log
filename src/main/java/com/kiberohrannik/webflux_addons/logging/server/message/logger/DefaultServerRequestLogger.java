package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@RequiredArgsConstructor
public final class DefaultServerRequestLogger implements ServerRequestLogger {

    private static final Log log = LogFactory.getLog(DefaultServerRequestLogger.class);

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> serverMessageFormatters;


    @Override
    public ServerHttpRequest log(ServerWebExchange exchange) {
        String baseMessage = "REQUEST: ".concat(exchange.getRequest().getMethodValue()).concat(" ")
                .concat(exchange.getRequest().getURI().toString());

        for (ServerMessageFormatter formatter : serverMessageFormatters) {
            baseMessage = formatter.addData(exchange, loggingProperties, baseMessage);
        }

        if (loggingProperties.isLogBody()) {
            return new LoggingServerHttpRequestDecorator(exchange.getRequest(), baseMessage);

        } else {
            log.info(baseMessage);
            return exchange.getRequest();
        }
    }
}