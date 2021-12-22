package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.ReqIdProvider;
import org.springframework.web.server.ServerWebExchange;

public final class ReqIdMessageFormatter implements ServerMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        return logProps.isLogRequestId()
                ? source.concat(provider.createMessage(exchange.getLogPrefix(), logProps))
                : source;
    }
}