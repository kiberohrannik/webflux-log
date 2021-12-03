package com.kiberohrannik.webflux_addons.logging.response.filter;

import com.kiberohrannik.webflux_addons.logging.request.message.formatter.extractor.RequestBodyExtractor;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseMessageCreator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogResponseFilter implements ExchangeFilterFunction {

    private static final Log log = LogFactory.getLog(LogResponseFilter.class);
    private final ResponseMessageCreator messageCreator;


    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .elapsed()
                .flatMap(tuple -> messageCreator.formatMessage(tuple.getT1(), tuple.getT2())
                        .doOnNext(responseData -> log.info(responseData.getLogMessage()))
                        .map(ResponseData::getResponse));
    }
}