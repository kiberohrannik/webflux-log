package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HeaderRequestMessageFormatterUnitTest extends BaseTest {

    private final HeaderClientResponseFormatter formatter = new HeaderClientResponseFormatter();

    private final ClientResponse response = ClientResponse.create(HttpStatus.OK)
            .header(HttpHeaders.ACCEPT, "application/json")
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header(HttpHeaders.AUTHORIZATION, "Some-Token")
            .header(HttpHeaders.AUTHORIZATION, "Any-Basic-Auth")
            .build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties properties = LoggingProperties.builder().logHeaders(false).build();

        String result = formatter.formatMessage(response, properties);
        assertEquals(LoggingUtils.EMPTY_MESSAGE, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithHeaders() {
        LoggingProperties properties = LoggingProperties.builder().logHeaders(true).build();

        String withHeaders = formatter.formatMessage(response, properties);
        assertAll(
                () -> assertNotNull(withHeaders),
                () -> assertTrue(withHeaders.contains("HEADERS:")),
                () -> assertTrue(withHeaders.contains("Accept=application/json")),
                () -> assertTrue(withHeaders.contains("Content-Type=application/json")),
                () -> assertTrue(withHeaders.contains("Authorization=Some-Token")),
                () -> assertTrue(withHeaders.contains("Authorization=Any-Basic-Auth"))
        );
    }

    @Test
    void addData_whenLogAndMaskHeaders_thenReturnWithMaskedHeaders() {
        LoggingProperties properties = LoggingProperties.builder()
                .logHeaders(true)
                .maskedHeaders(HttpHeaders.AUTHORIZATION, "AbsentHeader321")
                .build();

        String withHeaders = formatter.formatMessage(response, properties);
        assertAll(
                () -> assertNotNull(withHeaders),
                () -> assertTrue(withHeaders.contains("HEADERS:")),
                () -> assertTrue(withHeaders.contains("Accept=application/json")),
                () -> assertTrue(withHeaders.contains("Content-Type=application/json")),
                () -> assertTrue(withHeaders.contains("Authorization={masked}")),
                () -> assertFalse(withHeaders.contains("AbsentHeader321"))
        );
    }
}
