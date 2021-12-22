package com.kiberohrannik.webflux_addons.logging.client.request.filter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.request.message.*;
import com.kiberohrannik.webflux_addons.logging.client.request.message.formatter.*;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.ArrayList;
import java.util.List;

public class LogRequestFilterFactory {

    public static ExchangeFilterFunction defaultFilter(LoggingProperties loggingProperties) {
        List<RequestDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter(new HeaderProvider()));
        formatters.add(new CookieMessageFormatter(new CookieProvider()));
        formatters.add(new BodyMessageFormatter());

        return new LogRequestFilter(new BaseRequestMessageCreator(loggingProperties, formatters));
    }

    public static ExchangeFilterFunction defaultFilter(RequestMessageCreator requestMessageCreator) {
        return new LogRequestFilter(requestMessageCreator);
    }
}