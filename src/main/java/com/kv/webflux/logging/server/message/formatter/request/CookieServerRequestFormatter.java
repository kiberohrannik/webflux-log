package com.kv.webflux.logging.server.message.formatter.request;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.CookieProvider;
import com.kv.webflux.logging.server.message.formatter.ServerMetadataMessageFormatter;
import org.springframework.web.server.ServerWebExchange;

public final class CookieServerRequestFormatter implements ServerMetadataMessageFormatter {

    private final CookieProvider provider = new CookieProvider();


    @Override
    public String formatMessage(ServerWebExchange exchange, LoggingProperties properties) {
        return provider.createServerRequestMessage(exchange.getRequest().getCookies(), properties);
    }
}