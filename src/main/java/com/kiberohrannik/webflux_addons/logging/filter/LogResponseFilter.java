package com.kiberohrannik.webflux_addons.logging.filter;

import com.kiberohrannik.webflux_addons.old.LoggingExchangeParams;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public interface LogResponseFilter {

    ExchangeFilterFunction logResponse(LoggingExchangeParams loggingParams);
}