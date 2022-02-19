package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.CookieProvider;
import org.springframework.web.reactive.function.client.ClientResponse;

public class CookieClientResponseFormatter implements ResponseMetadataMessageFormatter {

    private final CookieProvider provider = new CookieProvider();


    @Override
    public String formatMessage(ClientResponse response, LoggingProperties properties) {
        return provider.createResponseMessage(response.cookies(), properties);
    }
}