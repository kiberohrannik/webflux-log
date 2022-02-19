package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientResponse;

public interface ResponseMetadataMessageFormatter {

    String formatMessage(ClientResponse response, LoggingProperties properties);
}