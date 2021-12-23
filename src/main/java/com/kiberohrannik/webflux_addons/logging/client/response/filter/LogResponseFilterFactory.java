package com.kiberohrannik.webflux_addons.logging.client.response.filter;


import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.BaseResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.*;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
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