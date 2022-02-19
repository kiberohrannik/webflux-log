package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.response.message.ResponseData;
import com.kv.webflux.logging.provider.CookieProvider;
import reactor.core.publisher.Mono;

public class CookieMessageFormatter implements ResponseDataMessageFormatter {

    private final CookieProvider provider = new CookieProvider();


    @Override
    public Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source) {
        if (logProps.isLogCookies()) {
            return source.map(message ->
                    message.addToLogs(provider.createResponseMessage(message.getResponse().cookies(), logProps)));
        }

        return source;
    }
}