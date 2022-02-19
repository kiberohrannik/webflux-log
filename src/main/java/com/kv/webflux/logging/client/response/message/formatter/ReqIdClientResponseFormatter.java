package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.ReqIdProvider;
import org.springframework.web.reactive.function.client.ClientResponse;

public class ReqIdClientResponseFormatter implements ResponseMetadataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public String formatMessage(ClientResponse response, LoggingProperties properties) {
        return provider.createFromLogPrefix(response.logPrefix(), properties);
    }
}