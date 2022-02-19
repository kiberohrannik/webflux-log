package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.response.message.ResponseData;
import reactor.core.publisher.Mono;

public interface ResponseDataMessageFormatter {

    Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source);
}