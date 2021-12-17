package com.kiberohrannik.webflux_addons.base;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.request.filter.LogRequestFilterFactory;
import com.kiberohrannik.webflux_addons.logging.request.message.BaseRequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.request.message.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.request.message.formatter.*;
import com.kiberohrannik.webflux_addons.logging.response.filter.LogResponseFilterFactory;
import com.kiberohrannik.webflux_addons.logging.response.message.BaseResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.ResponseDataMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.stub.RequestMessageCreatorTestDecorator;
import com.kiberohrannik.webflux_addons.logging.stub.ResponseMessageCreatorTestDecorator;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public abstract class BaseComponentTest extends BaseTest {

    private static final List<RequestDataMessageFormatter> requestLogMsgFormatters = List.of(
            new ReqIdMessageFormatter(),
            new HeaderMessageFormatter(),
            new CookieMessageFormatter(),
            new BodyMessageFormatter()
    );

    private static final List<ResponseDataMessageFormatter> responseLogMsgFormatters = List.of(
            new com.kiberohrannik.webflux_addons.logging.response.message.formatter.ReqIdMessageFormatter(),
            new com.kiberohrannik.webflux_addons.logging.response.message.formatter.HeaderMessageFormatter(),
            new com.kiberohrannik.webflux_addons.logging.response.message.formatter.CookieMessageFormatter(),
            new com.kiberohrannik.webflux_addons.logging.response.message.formatter.BodyMessageFormatter()
    );


    protected static WebClient createTestRequestLogWebClient(LoggingProperties logProperties,
                                                             @Nullable String requestBody) {

        RequestMessageCreator msgCreator = new BaseRequestMessageCreator(logProperties, requestLogMsgFormatters);

        RequestMessageCreatorTestDecorator testDecorator = new RequestMessageCreatorTestDecorator(
                msgCreator, logProperties, requestBody);

        ExchangeFilterFunction logRequestFilter = LogRequestFilterFactory.defaultFilter(testDecorator);

        return WebClient.builder()
                .filter(logRequestFilter)
                .build();
    }

    protected static WebClient createTestResponseLogWebClient(LoggingProperties logProperties,
                                                              @Nullable String responseBody) {

        ResponseMessageCreator msgCreator = new BaseResponseMessageCreator(logProperties, responseLogMsgFormatters);

        ResponseMessageCreatorTestDecorator testDecorator = new ResponseMessageCreatorTestDecorator(
                msgCreator, logProperties, responseBody);

        ExchangeFilterFunction logResponseFilter = LogResponseFilterFactory.defaultFilter(testDecorator);

        return WebClient.builder()
                .filter(logResponseFilter)
                .build();
    }
}
