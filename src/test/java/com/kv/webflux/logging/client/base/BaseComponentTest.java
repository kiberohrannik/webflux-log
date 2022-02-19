package com.kv.webflux.logging.client.base;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.request.filter.ClientRequestLoggingFilterFactory;
import com.kv.webflux.logging.client.request.message.BaseRequestMessageCreator;
import com.kv.webflux.logging.client.request.message.RequestMessageCreator;
import com.kv.webflux.logging.client.request.message.formatter.*;
import com.kv.webflux.logging.client.response.filter.ClientResponseLoggingFilterFactory;
import com.kv.webflux.logging.client.response.message.BaseResponseMessageCreator;
import com.kv.webflux.logging.client.response.message.ResponseMessageCreator;
import com.kv.webflux.logging.client.response.message.formatter.CookieClientResponseFormatter;
import com.kv.webflux.logging.client.response.message.formatter.HeaderClientResponseFormatter;
import com.kv.webflux.logging.client.response.message.formatter.ReqIdClientResponseFormatter;
import com.kv.webflux.logging.client.response.message.formatter.ResponseMetadataMessageFormatter;
import com.kv.webflux.logging.client.stub.RequestMessageCreatorTestDecorator;
import com.kv.webflux.logging.client.stub.ResponseMessageCreatorTestDecorator;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public abstract class BaseComponentTest extends BaseTest {

    private static final List<RequestDataMessageFormatter> requestFormatters = List.of(
            new ReqIdClientRequestFormatter(),
            new HeaderClientRequestFormatter(),
            new CookieClientRequestFormatter(),
            new BodyClientRequestFormatter()
    );

    private static final List<ResponseMetadataMessageFormatter> responseFormatters = List.of(
            new ReqIdClientResponseFormatter(),
            new HeaderClientResponseFormatter(),
            new CookieClientResponseFormatter()
    );


    protected static WebClient createTestRequestLogWebClient(LoggingProperties properties,
                                                             @Nullable String requestBody) {

        RequestMessageCreator messageCreator = new BaseRequestMessageCreator(properties, requestFormatters);
        RequestMessageCreatorTestDecorator testMessageCreator = new RequestMessageCreatorTestDecorator(
                messageCreator, properties, requestBody);

        ExchangeFilterFunction logRequestFilter = ClientRequestLoggingFilterFactory.customFilter(testMessageCreator);

        return WebClient.builder()
                .filter(logRequestFilter)
                .build();
    }

    protected static WebClient createTestResponseLogWebClient(LoggingProperties properties,
                                                              @Nullable String responseBody) {

        ResponseMessageCreator messageCreator = new BaseResponseMessageCreator(properties, responseFormatters);
        ResponseMessageCreatorTestDecorator testMessageCreator = new ResponseMessageCreatorTestDecorator(
                messageCreator, properties, responseBody);

        ExchangeFilterFunction logResponseFilter = ClientResponseLoggingFilterFactory.customFilter(testMessageCreator);

        return WebClient.builder()
                .filter(logResponseFilter)
                .build();
    }
}
