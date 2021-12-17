package com.kiberohrannik.webflux_addons.logging.request.filter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.request.message.*;
import com.kiberohrannik.webflux_addons.logging.request.message.formatter.*;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.ArrayList;
import java.util.List;

public class LogRequestFilterFactory {

    public static ExchangeFilterFunction defaultFilter(LoggingProperties loggingProperties) {
        List<RequestDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter());
        formatters.add(new CookieMessageFormatter());
        formatters.add(new BodyMessageFormatter());

        return new LogRequestFilter(new BaseRequestMessageCreator(loggingProperties, formatters));
    }

    public static ExchangeFilterFunction defaultFilter(RequestMessageCreator requestMessageCreator) {
        return new LogRequestFilter(requestMessageCreator);
    }
}