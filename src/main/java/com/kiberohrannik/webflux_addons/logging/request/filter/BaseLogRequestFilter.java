package com.kiberohrannik.webflux_addons.logging.request.filter;

import com.kiberohrannik.webflux_addons.logging.request.message.RequestMessageCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Slf4j  //TODO add ability to use any logger and configuration
@RequiredArgsConstructor
public class BaseLogRequestFilter implements LogRequestFilter {

    private final RequestMessageCreator messageCreator;


    @Override
    public ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request ->
                messageCreator.formatMessage(request)
                        .doOnNext(log::info)
                        .map(val -> request)
        );
    }
}