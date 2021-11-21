package com.kiberohrannik.webflux_addons.logging.creator;

import com.kiberohrannik.webflux_addons.logging.filter.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public interface RequestDataMessageFormatter {

    Mono<String> addData(ClientRequest request, LoggingProperties loggingProperties, Mono<String> sourceMessage);
}
