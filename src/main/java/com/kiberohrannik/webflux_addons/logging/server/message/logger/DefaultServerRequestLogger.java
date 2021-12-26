package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public final class DefaultServerRequestLogger implements ServerRequestLogger {

    private static final Log log = LogFactory.getLog(DefaultServerRequestLogger.class);

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> messageFormatters;


    public DefaultServerRequestLogger(LoggingProperties loggingProperties,
                                      List<ServerMessageFormatter> messageFormatters) {
        this.loggingProperties = loggingProperties;
        this.messageFormatters = messageFormatters;
    }


    @Override
    public ServerHttpRequest log(ServerWebExchange exchange) {
        String baseMessage = "REQUEST: ".concat(exchange.getRequest().getMethodValue()).concat(" ")
                .concat(exchange.getRequest().getURI().toString());

        for (ServerMessageFormatter formatter : messageFormatters) {
            baseMessage = formatter.addData(exchange, loggingProperties, baseMessage);
        }

        log.info(baseMessage);

        return loggingProperties.isLogBody()
                ? new LoggingServerHttpRequestDecorator(exchange.getRequest(), loggingProperties)
                : exchange.getRequest();
    }
}