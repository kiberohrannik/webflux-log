package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import com.kv.webflux.logging.util.TestUtils;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import static org.junit.jupiter.api.Assertions.*;

public class ReqIdClientResponseFormatterUnitTest extends BaseTest {

    private final ReqIdClientResponseFormatter formatter = new ReqIdClientResponseFormatter();

    private final ClientResponse response = ClientResponse.create(HttpStatus.OK).build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties properties = LoggingProperties.builder().logRequestId(false).build();

        String result = formatter.formatMessage(response, properties);
        assertEquals(LoggingUtils.EMPTY_MESSAGE, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqId() {
        LoggingProperties properties = LoggingProperties.builder().logRequestId(true).build();

        String withReqId = formatter.formatMessage(response, properties);
        assertAll(
                () -> assertNotNull(withReqId),
                () -> assertTrue(withReqId.contains("REQ-ID:")),
                () -> assertTrue(withReqId.contains(TestUtils.formatToLoggedReqId(response.logPrefix())))
        );
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqIdAndWithReqIdPrefix() {
        String reqIdPrefix = RandomString.make(20);
        LoggingProperties properties = LoggingProperties.builder()
                .logRequestId(true)
                .requestIdPrefix(reqIdPrefix)
                .build();

        String withReqId = formatter.formatMessage(response, properties);
        assertAll(
                () -> assertNotNull(withReqId),
                () -> assertTrue(withReqId.contains("REQ-ID:")),
                () -> assertTrue(withReqId.contains(TestUtils.formatToLoggedReqId(response.logPrefix(), reqIdPrefix)))
        );
    }
}