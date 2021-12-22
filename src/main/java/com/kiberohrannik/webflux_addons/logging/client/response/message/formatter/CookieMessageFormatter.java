package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CookieMessageFormatter implements ResponseDataMessageFormatter {

    private final CookieProvider provider;


    @Override
    public Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source) {
        if (logProps.isLogCookies()) {
            return source.map(message ->
                    message.addToLogs(provider.createResponseMessage(message.getResponse().cookies(), logProps)));
        }

        return source;
    }
}