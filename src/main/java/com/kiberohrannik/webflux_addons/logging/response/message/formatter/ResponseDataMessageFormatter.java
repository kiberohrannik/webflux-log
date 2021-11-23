package com.kiberohrannik.webflux_addons.logging.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface ResponseDataMessageFormatter {

    Mono<String> addData(ClientResponse response, LoggingProperties loggingProperties, Mono<String> sourceMessage);
}
