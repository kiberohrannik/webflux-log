package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.HeaderProvider;
import org.springframework.web.reactive.function.client.ClientResponse;

public class HeaderClientResponseFormatter implements ResponseMetadataMessageFormatter {

    private final HeaderProvider provider = new HeaderProvider();


    @Override
    public String formatMessage(ClientResponse response, LoggingProperties properties) {
        return provider.createMessage(response.headers().asHttpHeaders(), properties);
    }
}