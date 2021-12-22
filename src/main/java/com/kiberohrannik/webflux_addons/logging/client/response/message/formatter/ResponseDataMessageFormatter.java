package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import reactor.core.publisher.Mono;

public interface ResponseDataMessageFormatter {

    Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source);
}