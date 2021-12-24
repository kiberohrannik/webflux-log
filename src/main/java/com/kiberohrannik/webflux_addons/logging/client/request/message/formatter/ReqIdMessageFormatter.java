package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.ReqIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReqIdMessageFormatter implements RequestDataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties logProps, Mono<String> source) {
        return source.map(message -> provider.createFromLogPrefix(request.logPrefix(), logProps, message));
    }
}