package com.kv.webflux.logging.client.response.filter;

import com.kv.webflux.logging.client.response.message.ResponseData;
import com.kv.webflux.logging.client.response.message.ResponseMessageCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class ClientResponseLoggingFilter implements ExchangeFilterFunction {

    private static final Log log = LogFactory.getLog(ClientResponseLoggingFilter.class);
    private final ResponseMessageCreator messageCreator;


    public ClientResponseLoggingFilter(ResponseMessageCreator messageCreator) {
        this.messageCreator = messageCreator;
    }


    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .elapsed()
                .flatMap(tuple -> messageCreator.formatMessage(tuple.getT1(), tuple.getT2())
                        .doOnNext(responseData -> log.info(responseData.getLogMessage()))
                        .map(ResponseData::getResponse));
    }
}