package com.kv.webflux.logging.client.base;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.request.filter.ClientRequestLoggingFilterFactory;
import com.kv.webflux.logging.client.request.message.BaseRequestMessageCreator;
import com.kv.webflux.logging.client.request.message.RequestMessageCreator;
import com.kv.webflux.logging.client.request.message.formatter.RequestDataMessageFormatter;
import com.kv.webflux.logging.client.response.filter.ClientResponseLoggingFilterFactory;
import com.kv.webflux.logging.client.response.message.BaseResponseMessageCreator;
import com.kv.webflux.logging.client.response.message.ResponseMessageCreator;
import com.kv.webflux.logging.client.response.message.formatter.*;
import com.kv.webflux.logging.client.stub.RequestMessageCreatorTestDecorator;
import com.kv.webflux.logging.client.stub.ResponseMessageCreatorTestDecorator;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public abstract class BaseComponentTest extends BaseTest {

    private static final List<RequestDataMessageFormatter> requestLogMsgFormatters = List.of(
            new com.kv.webflux.logging.client.request.message.formatter.ReqIdMessageFormatter(),
            new com.kv.webflux.logging.client.request.message.formatter.HeaderMessageFormatter(),
            new com.kv.webflux.logging.client.request.message.formatter.CookieMessageFormatter(),
            new com.kv.webflux.logging.client.request.message.formatter.BodyMessageFormatter()
    );

    private static final List<ResponseDataMessageFormatter> responseLogMsgFormatters = List.of(
            new ReqIdMessageFormatter(),
            new HeaderMessageFormatter(),
            new CookieMessageFormatter(),
            new BodyMessageFormatter()
    );


    protected static WebClient createTestRequestLogWebClient(LoggingProperties logProperties,
                                                             @Nullable String requestBody) {

        RequestMessageCreator msgCreator = new BaseRequestMessageCreator(logProperties, requestLogMsgFormatters);

        RequestMessageCreatorTestDecorator testDecorator = new RequestMessageCreatorTestDecorator(
                msgCreator, logProperties, requestBody);

        ExchangeFilterFunction logRequestFilter = ClientRequestLoggingFilterFactory.customFilter(testDecorator);

        return WebClient.builder()
                .filter(logRequestFilter)
                .build();
    }

    protected static WebClient createTestResponseLogWebClient(LoggingProperties logProperties,
                                                              @Nullable String responseBody) {

        ResponseMessageCreator msgCreator = new BaseResponseMessageCreator(logProperties, responseLogMsgFormatters);

        ResponseMessageCreatorTestDecorator testDecorator = new ResponseMessageCreatorTestDecorator(
                msgCreator, logProperties, responseBody);

        ExchangeFilterFunction logResponseFilter = ClientResponseLoggingFilterFactory.customFilter(testDecorator);

        return WebClient.builder()
                .filter(logResponseFilter)
                .build();
    }
}
