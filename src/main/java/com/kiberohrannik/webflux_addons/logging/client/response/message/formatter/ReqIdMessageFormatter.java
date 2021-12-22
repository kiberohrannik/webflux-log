package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.provider.ReqIdProvider;
import reactor.core.publisher.Mono;

public class ReqIdMessageFormatter implements ResponseDataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source) {
        if (logProps.isLogRequestId()) {
            return source.map(message ->
                    message.addToLogs(provider.createMessage(message.getResponse().logPrefix(), logProps)));
        }

        return source;
    }
}