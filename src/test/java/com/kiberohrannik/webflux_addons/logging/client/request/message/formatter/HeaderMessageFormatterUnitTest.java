package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.extractor.HeaderExtractor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class HeaderMessageFormatterUnitTest extends BaseTest {

    private final HeaderMessageFormatter formatter = new HeaderMessageFormatter(new HeaderExtractor());

    private final ClientRequest testRequest = ClientRequest.create(HttpMethod.GET, URI.create("/someUri"))
            .header(HttpHeaders.ACCEPT, "application/json")
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header(HttpHeaders.AUTHORIZATION, "Some-Token")
            .header(HttpHeaders.AUTHORIZATION, "Any-Basic-Auth")
            .build();

    private final String sourceMessage = RandomString.make();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logHeaders(false).build();

        String result = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(result);
        assertEquals(sourceMessage, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithHeaders() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logHeaders(true).build();

        String withHeaders = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
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
        LoggingProperties loggingProperties = LoggingProperties.builder()
                .logHeaders(true)
                .maskedHeaders(HttpHeaders.AUTHORIZATION, "AbsentHeader321")
                .build();

        String withHeaders = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
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
