package com.kiberohrannik.webflux_addons.logging.request.filter;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public interface LogRequestFilter {

    ExchangeFilterFunction logRequest();
}