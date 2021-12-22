package com.kiberohrannik.webflux_addons.logging.server.message.formatter.response;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;

@RequiredArgsConstructor
public final class CookieResponseMessageFormatter implements ServerMessageFormatter {

    private final CookieProvider provider;

    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        return logProps.isLogCookies()
                ? source.concat(provider.createResponseMessage(exchange.getResponse().getCookies(), logProps))
                : source;
    }
}