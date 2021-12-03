package com.kiberohrannik.webflux_addons.base;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.request.filter.LogRequestFilter;
import com.kiberohrannik.webflux_addons.logging.request.filter.LogRequestFilterFactory;
import com.kiberohrannik.webflux_addons.logging.request.message.BaseRequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.request.message.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.request.message.formatter.*;
import com.kiberohrannik.webflux_addons.logging.stub.RequestMessageCreatorTestDecorator;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public abstract class BaseComponentTest extends BaseTest {

    private static final List<RequestDataMessageFormatter> logMessageFormatters = List.of(
            new ReqIdMessageFormatter(),
            new HeaderMessageFormatter(),
            new CookieMessageFormatter(),
            new BodyMessageFormatter()
    );


    protected static WebClient createTestWebClient(LoggingProperties loggingProperties, @Nullable String requestBody) {
        RequestMessageCreator messageCreator = new BaseRequestMessageCreator(loggingProperties, logMessageFormatters);

        RequestMessageCreatorTestDecorator testDecorator =
                new RequestMessageCreatorTestDecorator(messageCreator, loggingProperties, requestBody);

        LogRequestFilter logRequestFilter = LogRequestFilterFactory.defaultFilter(testDecorator);

        return WebClient.builder()
                .filter(logRequestFilter.logRequest())
                .build();
    }
}
