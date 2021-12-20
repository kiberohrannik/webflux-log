package com.kiberohrannik.webflux_addons.logging.client.request.filter;

import com.kiberohrannik.webflux_addons.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.request.message.BaseRequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.request.message.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.client.request.message.formatter.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.getField;

public class LogRequestFilterFactoryUnitTest extends BaseTest {

    @Test
    void createDefaultFilter() {
        LoggingProperties logProps = LoggingProperties.builder().build();

        ExchangeFilterFunction filterFunction = LogRequestFilterFactory.defaultFilter(logProps);
        assertTrue(filterFunction instanceof LogRequestFilter);

        LogRequestFilter logFilter = (LogRequestFilter) filterFunction;

        RequestMessageCreator messageCreator = (RequestMessageCreator) getField(logFilter, "messageCreator");
        assertTrue(messageCreator instanceof BaseRequestMessageCreator);

        List<RequestDataMessageFormatter> messageFormatters =
                (List<RequestDataMessageFormatter>) getField(messageCreator, "messageFormatters");

        assertNotNull(messageFormatters);
        assertEquals(4, messageFormatters.size());

        assertTrue(messageFormatters.stream().anyMatch(formatter -> formatter instanceof HeaderMessageFormatter));
        assertTrue(messageFormatters.stream().anyMatch(formatter -> formatter instanceof CookieMessageFormatter));
        assertTrue(messageFormatters.stream().anyMatch(formatter -> formatter instanceof ReqIdMessageFormatter));
        assertTrue(messageFormatters.stream().anyMatch(formatter -> formatter instanceof BodyMessageFormatter));
    }
}