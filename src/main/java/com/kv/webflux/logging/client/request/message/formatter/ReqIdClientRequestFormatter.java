package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.ReqIdProvider;
import org.springframework.web.reactive.function.client.ClientRequest;

public class ReqIdClientRequestFormatter implements RequestMetadataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public String formatMessage(ClientRequest request, LoggingProperties properties) {
        return provider.createFromLogPrefix(request.logPrefix(), properties);
    }
}