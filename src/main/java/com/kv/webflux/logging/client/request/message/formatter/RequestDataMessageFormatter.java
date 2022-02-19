package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public interface RequestDataMessageFormatter {

    Mono<String> addData(ClientRequest request, LoggingProperties properties);
}
