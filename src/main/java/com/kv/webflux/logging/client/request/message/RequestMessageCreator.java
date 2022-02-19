package com.kv.webflux.logging.client.request.message;

import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public interface RequestMessageCreator {

    Mono<String> createMessage(ClientRequest request);
}
