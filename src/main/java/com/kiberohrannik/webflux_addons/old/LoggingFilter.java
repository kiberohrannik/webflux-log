package com.kiberohrannik.webflux_addons.old;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public interface LoggingFilter {

    ExchangeFilterFunction logRequest(LoggingExchangeParams loggingParams);

    ExchangeFilterFunction logResponse(LoggingExchangeParams loggingParams);
}