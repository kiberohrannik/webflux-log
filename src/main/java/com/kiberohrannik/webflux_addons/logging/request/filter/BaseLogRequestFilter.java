package com.kiberohrannik.webflux_addons.logging.request.filter;

import com.kiberohrannik.webflux_addons.logging.request.message.formatter.extractor.RequestBodyExtractor;
import com.kiberohrannik.webflux_addons.logging.request.message.RequestMessageCreator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@RequiredArgsConstructor
public class BaseLogRequestFilter implements LogRequestFilter {

    private static final Log log = LogFactory.getLog(BaseLogRequestFilter.class);
    private final RequestMessageCreator messageCreator;


    @Override
    public ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request ->
                messageCreator.formatMessage(request)
                        .doOnNext(log::info)
                        .map(val -> request)
        );
    }
}