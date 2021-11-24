package com.kiberohrannik.webflux_addons.logging.response.message;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface ResponseMessageCreator {

    Mono<ResponseData> formatMessage(Long responseTimeMillis, ClientResponse response);
}
