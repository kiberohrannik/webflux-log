package com.kiberohrannik.webflux_addons.logging.server.message.formatter.request;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;

@RequiredArgsConstructor
public final class CookieRequestMessageFormatter implements ServerMessageFormatter {

    private final CookieProvider provider;


    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        return logProps.isLogCookies()
                ? source.concat(provider.createServerRequestMessage(exchange.getRequest().getCookies(), logProps))
                : source;
    }
}