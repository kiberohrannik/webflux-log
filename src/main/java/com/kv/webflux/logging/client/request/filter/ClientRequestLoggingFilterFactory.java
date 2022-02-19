package com.kv.webflux.logging.client.request.filter;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.request.message.BaseRequestMessageCreator;
import com.kv.webflux.logging.client.request.message.RequestMessageCreator;
import com.kv.webflux.logging.client.request.message.formatter.*;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.ArrayList;
import java.util.List;

public class ClientRequestLoggingFilterFactory {

    public static ExchangeFilterFunction defaultFilter(LoggingProperties loggingProperties) {
        List<RequestDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdClientRequestFormatter());
        formatters.add(new HeaderClientRequestFormatter());
        formatters.add(new CookieClientRequestFormatter());
        formatters.add(new BodyClientRequestFormatter());

        return new ClientRequestLoggingFilter(new BaseRequestMessageCreator(loggingProperties, formatters));
    }

    public static ExchangeFilterFunction customFilter(RequestMessageCreator requestMessageCreator) {
        return new ClientRequestLoggingFilter(requestMessageCreator);
    }
}