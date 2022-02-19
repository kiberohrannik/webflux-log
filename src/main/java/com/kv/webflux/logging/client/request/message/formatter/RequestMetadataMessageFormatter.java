package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;

public interface RequestMetadataMessageFormatter {

    String formatMessage(ClientRequest request, LoggingProperties properties);
}
