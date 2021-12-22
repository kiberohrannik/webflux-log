package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public interface RequestDataMessageFormatter {

    Mono<String> addData(ClientRequest request, LoggingProperties logProps, Mono<String> source);
}
