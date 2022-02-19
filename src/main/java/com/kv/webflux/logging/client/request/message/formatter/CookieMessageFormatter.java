package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.CookieProvider;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class CookieMessageFormatter implements RequestDataMessageFormatter {

    private final CookieProvider provider = new CookieProvider();


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties properties) {
        return Mono.just(provider.createClientRequestMessage(request.cookies(), properties));
    }
}