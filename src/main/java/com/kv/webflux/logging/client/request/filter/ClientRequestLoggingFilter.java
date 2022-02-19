package com.kv.webflux.logging.client.request.filter;

import com.kv.webflux.logging.client.request.message.RequestMessageCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class ClientRequestLoggingFilter implements ExchangeFilterFunction {

    private static final Log log = LogFactory.getLog(ClientRequestLoggingFilter.class);
    private final RequestMessageCreator messageCreator;


    public ClientRequestLoggingFilter(RequestMessageCreator messageCreator) {
        this.messageCreator = messageCreator;
    }


    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return messageCreator.formatMessage(request)
                .doOnNext(log::info)
                .then(next.exchange(request));
    }
}