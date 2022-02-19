package com.kv.webflux.logging.client.request.filter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.request.message.BaseRequestMessageCreator;
import com.kv.webflux.logging.client.request.message.RequestMessageCreator;
import com.kv.webflux.logging.client.request.message.formatter.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.getField;

public class ClientRequestLoggingFilterFactoryUnitTest extends BaseTest {

    @Test
    void createDefaultFilter() {
        LoggingProperties properties = LoggingProperties.builder().build();

        ExchangeFilterFunction filterFunction = ClientRequestLoggingFilterFactory.defaultFilter(properties);
        assertTrue(filterFunction instanceof ClientRequestLoggingFilter);

        ClientRequestLoggingFilter logFilter = (ClientRequestLoggingFilter) filterFunction;

        RequestMessageCreator messageCreator = (RequestMessageCreator) getField(logFilter, "messageCreator");
        assertTrue(messageCreator instanceof BaseRequestMessageCreator);

        List<RequestMetadataMessageFormatter> metadataFormatters =
                (List<RequestMetadataMessageFormatter>) getField(messageCreator, "metadataFormatters");

        assertNotNull(metadataFormatters);
        assertEquals(3, metadataFormatters.size());

        assertTrue(metadataFormatters.stream().anyMatch(f -> f instanceof HeaderClientRequestFormatter));
        assertTrue(metadataFormatters.stream().anyMatch(f -> f instanceof CookieClientRequestFormatter));
        assertTrue(metadataFormatters.stream().anyMatch(f -> f instanceof ReqIdClientRequestFormatter));

        assertNotNull(getField(messageCreator, "bodyFormatter"));
    }
}