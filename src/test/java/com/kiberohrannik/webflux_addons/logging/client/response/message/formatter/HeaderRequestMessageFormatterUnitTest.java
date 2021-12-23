package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

public class HeaderRequestMessageFormatterUnitTest extends BaseTest {

    private final HeaderMessageFormatter formatter = new HeaderMessageFormatter();

    private final ClientResponse response = ClientResponse.create(HttpStatus.OK)
            .header(HttpHeaders.ACCEPT, "application/json")
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header(HttpHeaders.AUTHORIZATION, "Some-Token")
            .header(HttpHeaders.AUTHORIZATION, "Any-Basic-Auth")
            .build();

    private final String sourceLogMessage = RandomString.make();
    private final ResponseData sourceResponseData = new ResponseData(response, sourceLogMessage);


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logHeaders(false).build();

        ResponseData result = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertEquals(sourceLogMessage, result.getLogMessage());
    }

    @Test
    void addData_whenNeedLog_thenReturnWithHeaders() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logHeaders(true).build();

        ResponseData withHeaders = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(withHeaders);

        String actualLogMessage = withHeaders.getLogMessage();
        assertAll(
                () -> assertTrue(actualLogMessage.contains("HEADERS:")),
                () -> assertTrue(actualLogMessage.contains("Accept=application/json")),
                () -> assertTrue(actualLogMessage.contains("Content-Type=application/json")),
                () -> assertTrue(actualLogMessage.contains("Authorization=Some-Token")),
                () -> assertTrue(actualLogMessage.contains("Authorization=Any-Basic-Auth"))
        );
    }

    @Test
    void addData_whenLogAndMaskHeaders_thenReturnWithMaskedHeaders() {
        LoggingProperties loggingProperties = LoggingProperties.builder()
                .logHeaders(true)
                .maskedHeaders(HttpHeaders.AUTHORIZATION, "AbsentHeader321")
                .build();

        ResponseData withHeaders = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(withHeaders);

        String actualLogMessage = withHeaders.getLogMessage();
        assertAll(
                () -> assertTrue(actualLogMessage.contains("HEADERS:")),
                () -> assertTrue(actualLogMessage.contains("Accept=application/json")),
                () -> assertTrue(actualLogMessage.contains("Content-Type=application/json")),
                () -> assertTrue(actualLogMessage.contains("Authorization={masked}")),
                () -> assertFalse(actualLogMessage.contains("AbsentHeader321"))
        );
    }
}
