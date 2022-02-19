package com.kv.webflux.logging.client.response.filter;


import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.response.message.BaseResponseMessageCreator;
import com.kv.webflux.logging.client.response.message.ResponseMessageCreator;
import com.kv.webflux.logging.client.response.message.formatter.*;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.ArrayList;
import java.util.List;

public class ClientResponseLoggingFilterFactory {

    public static ExchangeFilterFunction defaultFilter(LoggingProperties loggingProperties) {
        List<ResponseDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter());
        formatters.add(new CookieMessageFormatter());
        formatters.add(new BodyMessageFormatter());

        return new ClientResponseLoggingFilter(new BaseResponseMessageCreator(loggingProperties, formatters));
    }

    public static ExchangeFilterFunction customFilter(ResponseMessageCreator responseMessageCreator) {
        return new ClientResponseLoggingFilter(responseMessageCreator);
    }
}