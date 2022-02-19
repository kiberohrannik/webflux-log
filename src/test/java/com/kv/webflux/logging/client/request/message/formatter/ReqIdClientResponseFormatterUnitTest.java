package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.net.URI;

import static com.kv.webflux.logging.client.LoggingUtils.EMPTY_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

public class ReqIdClientResponseFormatterUnitTest extends BaseTest {

    private final ReqIdClientRequestFormatter formatter = new ReqIdClientRequestFormatter();

    private final ClientRequest testRequest = ClientRequest.create(HttpMethod.GET, URI.create("/someUri")).build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties properties = LoggingProperties.builder().logRequestId(false).build();

        String result = formatter.addData(testRequest, properties).block();
        assertNotNull(result);
        assertEquals(EMPTY_MESSAGE, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqId() {
        LoggingProperties properties = LoggingProperties.builder().logRequestId(true).build();

        String withReqId = formatter.addData(testRequest, properties).block();
        assertNotNull(withReqId);
        assertAll(
                () -> assertTrue(withReqId.contains("REQ-ID:")),
                () -> assertTrue(withReqId.contains(formatToLoggedReqId(testRequest.logPrefix())))
        );
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqIdAndWithReqIdPrefix() {
        String reqIdPrefix = RandomString.make(20);
        LoggingProperties properties = LoggingProperties.builder()
                .logRequestId(true)
                .requestIdPrefix(reqIdPrefix)
                .build();

        String withReqId = formatter.addData(testRequest, properties).block();
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