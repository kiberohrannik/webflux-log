package com.kiberohrannik.webflux_addons.logging.client.request.filter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.request.message.BaseRequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.request.message.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.request.message.formatter.*;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.ArrayList;
import java.util.List;

public class ClientRequestLoggingFilterFactory {

    public static ExchangeFilterFunction defaultFilter(LoggingProperties loggingProperties) {
        List<RequestDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter());
        formatters.add(new CookieMessageFormatter());
        formatters.add(new BodyMessageFormatter());

        return new ClientRequestLoggingFilter(new BaseRequestMessageCreator(loggingProperties, formatters));
    }

    public static ExchangeFilterFunction defaultFilter(RequestMessageCreator requestMessageCreator) {
        return new ClientRequestLoggingFilter(requestMessageCreator);
    }
}