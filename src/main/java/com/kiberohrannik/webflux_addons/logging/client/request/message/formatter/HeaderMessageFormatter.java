package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class HeaderMessageFormatter implements RequestDataMessageFormatter {

    private final HeaderProvider provider;


    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties logProps, Mono<String> source) {
        if (logProps.isLogHeaders()) {
            return source.map(message -> {
                MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(request.headers());
                return message.concat(provider.createMessage(headersToLog, logProps));
            });
        }

        return source;
    }
}