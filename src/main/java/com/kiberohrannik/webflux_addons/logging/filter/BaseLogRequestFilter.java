package com.kiberohrannik.webflux_addons.logging.filter;

import com.kiberohrannik.webflux_addons.logging.creator.RequestMessageCreator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Slf4j  //TODO add ability to use any logger and configuration
@AllArgsConstructor
public class BaseLogRequestFilter implements LogRequestFilter {

    private final RequestMessageCreator messageCreator;


    @Override
    public ExchangeFilterFunction logRequest(LoggingProperties loggingProperties) {
        return ExchangeFilterFunction.ofRequestProcessor(request ->
                messageCreator.formatMessage(request, loggingProperties)
                        .doOnNext(log::info)
                        .map(val -> request)
        );
    }
}