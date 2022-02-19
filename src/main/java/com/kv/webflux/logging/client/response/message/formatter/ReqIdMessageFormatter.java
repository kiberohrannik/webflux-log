package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.response.message.ResponseData;
import com.kv.webflux.logging.provider.ReqIdProvider;
import reactor.core.publisher.Mono;

public class ReqIdMessageFormatter implements ResponseDataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source) {
        return source.map(data -> {
            String msg = provider.createFromLogPrefix(data.getResponse().logPrefix(), logProps, data.getLogMessage());
            data.setLogMessage(msg);
            return data;
        });
    }
}