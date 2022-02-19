package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.HeaderProvider;
import org.springframework.web.reactive.function.client.ClientRequest;

public class HeaderClientRequestFormatter implements RequestMetadataMessageFormatter {

    private final HeaderProvider provider = new HeaderProvider();


    @Override
    public String formatMessage(ClientRequest request, LoggingProperties properties) {
        return provider.createMessage(request.headers(), properties);
    }
}