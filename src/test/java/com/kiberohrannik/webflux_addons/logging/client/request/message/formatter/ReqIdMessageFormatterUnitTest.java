package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class ReqIdMessageFormatterUnitTest extends BaseTest {

    private final ReqIdMessageFormatter formatter = new ReqIdMessageFormatter();

    private final ClientRequest testRequest = ClientRequest.create(HttpMethod.GET, URI.create("/someUri")).build();
    private final String sourceMessage = RandomString.make();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logRequestId(false).build();

        String result = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(result);
        assertEquals(sourceMessage, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqId() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logRequestId(true).build();

        String withReqId = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(withReqId);
        assertAll(
                () -> assertTrue(withReqId.contains("REQ-ID:")),
                () -> assertTrue(withReqId.contains(formatToLoggedReqId(testRequest.logPrefix())))
        );
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqIdAndWithReqIdPrefix() {
        String reqIdPrefix = RandomString.make(20);
        LoggingProperties loggingProperties = LoggingProperties.builder()
                .logRequestId(true)
                .requestIdPrefix(reqIdPrefix)
                .build();

        String withReqId = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(withReqId);
        assertAll(
                () -> assertTrue(withReqId.contains("REQ-ID:")),
                () -> assertTrue(withReqId.contains(reqIdPrefix + "_" + formatToLoggedReqId(testRequest.logPrefix())))
        );
    }

    private String formatToLoggedReqId(String logPrefix) {
        return logPrefix.replaceAll("[\\[\\]\\s]", "");
    }
}