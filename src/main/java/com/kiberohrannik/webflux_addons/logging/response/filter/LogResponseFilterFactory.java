package com.kiberohrannik.webflux_addons.logging.response.filter;


import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.response.message.BaseResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.*;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.ArrayList;
import java.util.List;

public class LogResponseFilterFactory {

    public static ExchangeFilterFunction defaultFilter(LoggingProperties loggingProperties) {
        List<ResponseDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter());
        formatters.add(new CookieMessageFormatter());
        formatters.add(new BodyMessageFormatter());

        return new LogResponseFilter(new BaseResponseMessageCreator(loggingProperties, formatters));
    }

    public static ExchangeFilterFunction defaultFilter(ResponseMessageCreator responseMessageCreator) {
        return new LogResponseFilter(responseMessageCreator);
    }
}