package com.kv.webflux.logging.server.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.ReqIdProvider;
import org.springframework.web.server.ServerWebExchange;

public final class ReqIdServerFormatter implements ServerMetadataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public String formatMessage(ServerWebExchange exchange, LoggingProperties properties) {
        return provider.createFromLogPrefix(exchange.getLogPrefix(), properties);
    }
}