package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.CookieProvider;
import org.springframework.web.reactive.function.client.ClientRequest;

public class CookieClientRequestFormatter implements RequestMetadataMessageFormatter {

    private final CookieProvider provider = new CookieProvider();


    @Override
    public String formatMessage(ClientRequest request, LoggingProperties properties) {
        return provider.createClientRequestMessage(request.cookies(), properties);
    }
}