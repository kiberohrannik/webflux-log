package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CookieMessageFormatter implements RequestDataMessageFormatter {

    private final CookieProvider provider;


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties logProps, Mono<String> source) {
        if (logProps.isLogCookies()) {
            return source.map(message ->
                    message.concat(provider.createClientRequestMessage(request.cookies(), logProps)));
        }

        return source;
    }
}