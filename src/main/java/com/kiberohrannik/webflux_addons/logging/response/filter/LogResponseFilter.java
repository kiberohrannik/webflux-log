package com.kiberohrannik.webflux_addons.logging.response.filter;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public interface LogResponseFilter {

    ExchangeFilterFunction logResponse();
}
