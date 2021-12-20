package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static com.kiberohrannik.webflux_addons.logging.client.util.TestUtils.formatToLoggedReqId;
import static org.junit.jupiter.api.Assertions.*;

public class ReqIdMessageFormatterUnitTest extends BaseTest {

    private final ReqIdMessageFormatter formatter = new ReqIdMessageFormatter();

    private final ClientResponse response = ClientResponse.create(HttpStatus.OK).build();
    private final String sourceLogMessage = RandomString.make(20);
    private final ResponseData sourceResponseData = new ResponseData(response, sourceLogMessage);


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logRequestId(false).build();

        ResponseData result = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertEquals(sourceLogMessage, result.getLogMessage());
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqId() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logRequestId(true).build();

        ResponseData withReqId = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(withReqId);
        assertAll(
                () -> assertNotNull(withReqId.getResponse()),
                () -> assertTrue(withReqId.getLogMessage().contains("REQ-ID:")),
                () -> assertTrue(withReqId.getLogMessage().contains(formatToLoggedReqId(response.logPrefix())))
        );
    }

    @Test
    void addData_whenNeedLog_thenReturnWithReqIdAndWithReqIdPrefix() {
        String reqIdPrefix = RandomString.make(20);
        LoggingProperties loggingProperties = LoggingProperties.builder()
                .logRequestId(true)
                .requestIdPrefix(reqIdPrefix)
                .build();

        ResponseData withReqId = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(withReqId);
        assertAll(
                () -> assertNotNull(withReqId.getResponse()),
                () -> assertTrue(withReqId.getLogMessage().contains("REQ-ID:")),
                () -> assertTrue(withReqId.getLogMessage()
                        .contains(formatToLoggedReqId(response.logPrefix(), reqIdPrefix)))
        );
    }
}