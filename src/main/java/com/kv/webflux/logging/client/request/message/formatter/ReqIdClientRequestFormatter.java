package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.ReqIdProvider;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class ReqIdClientRequestFormatter implements RequestDataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties properties) {
        return Mono.just(provider.createFromLogPrefix(request.logPrefix(), properties));
    }
}