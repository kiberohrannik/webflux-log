package com.kv.webflux.logging.server.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.ReqIdProvider;
import org.springframework.web.server.ServerWebExchange;

public final class ReqIdMessageFormatter implements ServerMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        return provider.createFromLogPrefix(exchange.getLogPrefix(), logProps, source);
    }
}