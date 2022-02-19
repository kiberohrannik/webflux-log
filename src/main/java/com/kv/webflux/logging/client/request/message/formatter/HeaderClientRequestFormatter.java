package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.HeaderProvider;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class HeaderClientRequestFormatter implements RequestDataMessageFormatter {

    private final HeaderProvider provider = new HeaderProvider();


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties properties) {
        return Mono.just(provider.createMessage(request.headers(), properties));
    }
}