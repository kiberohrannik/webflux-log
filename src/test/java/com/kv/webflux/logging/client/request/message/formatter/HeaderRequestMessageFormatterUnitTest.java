package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.net.URI;

import static com.kv.webflux.logging.client.LoggingUtils.EMPTY_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

public class HeaderRequestMessageFormatterUnitTest extends BaseTest {

    private final HeaderClientRequestFormatter formatter = new HeaderClientRequestFormatter();

    private final ClientRequest testRequest = ClientRequest.create(HttpMethod.GET, URI.create("/someUri"))
            .header(HttpHeaders.ACCEPT, "application/json")
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header(HttpHeaders.AUTHORIZATION, "Some-Token")
            .header(HttpHeaders.AUTHORIZATION, "Any-Basic-Auth")
            .build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties properties = LoggingProperties.builder().logHeaders(false).build();

        String result = formatter.addData(testRequest, properties).block();
        assertNotNull(result);
        assertEquals(EMPTY_MESSAGE, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithHeaders() {
        LoggingProperties properties = LoggingProperties.builder().logHeaders(true).build();

        String withHeaders = formatter.addData(testRequest, properties).block();
        assertNotNull(withHeaders);
        assertAll(
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

        String withHeaders = formatter.addData(testRequest, properties).block();
        assertNotNull(withHeaders);
        assertAll(
                () -> assertTrue(withHeaders.contains("HEADERS:")),
                () -> assertTrue(withHeaders.contains("Accept=application/json")),
                () -> assertTrue(withHeaders.contains("Content-Type=application/json")),
                () -> assertTrue(withHeaders.contains("Authorization={masked}")),
                () -> assertFalse(withHeaders.contains("AbsentHeader321"))
        );
    }
}
