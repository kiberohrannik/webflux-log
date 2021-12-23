package com.kiberohrannik.webflux_addons.logging.client.base;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.request.filter.LogRequestFilterFactory;
import com.kiberohrannik.webflux_addons.logging.client.request.message.BaseRequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.request.message.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.request.message.formatter.*;
import com.kiberohrannik.webflux_addons.logging.client.response.filter.LogResponseFilterFactory;
import com.kiberohrannik.webflux_addons.logging.client.response.message.BaseResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.ResponseDataMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.client.stub.RequestMessageCreatorTestDecorator;
import com.kiberohrannik.webflux_addons.logging.client.stub.ResponseMessageCreatorTestDecorator;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public abstract class BaseComponentTest extends BaseTest {

    private static final List<RequestDataMessageFormatter> requestLogMsgFormatters = List.of(
            new ReqIdMessageFormatter(),
            new HeaderMessageFormatter(new HeaderProvider()),
            new CookieMessageFormatter(new CookieProvider()),
            new BodyMessageFormatter()
    );

    private static final List<ResponseDataMessageFormatter> responseLogMsgFormatters = List.of(
            new com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.ReqIdMessageFormatter(),
            new com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.HeaderMessageFormatter(),
            new com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.CookieMessageFormatter(),
            new com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.BodyMessageFormatter()
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
