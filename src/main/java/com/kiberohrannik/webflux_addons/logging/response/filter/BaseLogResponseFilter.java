package com.kiberohrannik.webflux_addons.logging.response.filter;

import com.kiberohrannik.webflux_addons.logging.request.message.RequestBodyExtractor;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseMessageCreator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@RequiredArgsConstructor
public class BaseLogResponseFilter implements LogResponseFilter {

    private static final Log log = LogFactory.getLog(RequestBodyExtractor.class);
    private final ResponseMessageCreator messageCreator;


    @Override
    public ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response ->
                messageCreator.formatMessage(response)
                        .doOnNext(log::info)
                        .map(val -> response)
        );
    }
}