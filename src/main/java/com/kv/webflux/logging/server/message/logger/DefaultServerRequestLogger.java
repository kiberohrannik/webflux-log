package com.kv.webflux.logging.server.message.logger;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.server.message.formatter.ServerMetadataMessageFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public final class DefaultServerRequestLogger implements ServerRequestLogger {

    private static final Log log = LogFactory.getLog(DefaultServerRequestLogger.class);

    private final LoggingProperties properties;
    private final List<ServerMetadataMessageFormatter> messageFormatters;


    public DefaultServerRequestLogger(LoggingProperties properties,
                                      List<ServerMetadataMessageFormatter> messageFormatters) {
        this.properties = properties;
        this.messageFormatters = messageFormatters;
    }


    @Override
    public ServerHttpRequest log(ServerWebExchange exchange) {
        StringBuilder metadata = new StringBuilder("REQUEST: ")
                .append(exchange.getRequest().getMethodValue())
                .append(" ")
                .append(exchange.getRequest().getURI());

        for (ServerMetadataMessageFormatter formatter : messageFormatters) {
            metadata.append(formatter.formatMessage(exchange, properties));
        }

        log.info(metadata.toString());

        return properties.isLogBody()
                ? new LoggingServerHttpRequestDecorator(exchange.getRequest(), properties)
                : exchange.getRequest();
    }
}