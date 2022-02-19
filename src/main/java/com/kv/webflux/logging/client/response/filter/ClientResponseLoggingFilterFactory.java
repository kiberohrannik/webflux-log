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
        List<ResponseMetadataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdClientResponseFormatter());
        formatters.add(new HeaderClientResponseFormatter());
        formatters.add(new CookieClientResponseFormatter());

        return new ClientResponseLoggingFilter(
                new BaseResponseMessageCreator(loggingProperties, formatters, new BodyFormatter())
        );
    }

    public static ExchangeFilterFunction customFilter(ResponseMessageCreator responseMessageCreator) {
        return new ClientResponseLoggingFilter(responseMessageCreator);
    }
}