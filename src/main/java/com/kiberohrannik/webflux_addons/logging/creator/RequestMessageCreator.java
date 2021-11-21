package com.kiberohrannik.webflux_addons.logging.creator;

import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public interface RequestMessageCreator {

    Mono<String> formatMessage(ClientRequest request);
}
