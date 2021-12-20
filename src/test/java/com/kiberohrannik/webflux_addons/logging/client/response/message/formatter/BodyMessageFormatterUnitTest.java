package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static com.kiberohrannik.webflux_addons.logging.client.LoggingUtils.NO_BODY_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

public class BodyMessageFormatterUnitTest extends BaseTest {

    private final BodyMessageFormatter formatter = new BodyMessageFormatter();

    private final String bodyStr = RandomString.make();
    private final ClientResponse response = ClientResponse.create(HttpStatus.BAD_REQUEST)
            .body(bodyStr)
            .build();

    private final String sourceLogMessage = RandomString.make();
    private final ResponseData sourceResponseData = new ResponseData(response, sourceLogMessage);


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logBody(false).build();

        ResponseData result = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(result);
        assertEquals(sourceLogMessage, result.getLogMessage());
    }

    @Test
    void addData_whenNoBody_thenAddEmpty() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logBody(true).build();
        ClientResponse responseWithNoBody = ClientResponse.create(HttpStatus.BAD_REQUEST).build();
        ResponseData sourceResponseData = new ResponseData(responseWithNoBody, sourceLogMessage);

        ResponseData result = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(result);
        assertTrue(result.getLogMessage().contains("BODY:"));
        assertTrue(result.getLogMessage().contains(NO_BODY_MESSAGE));
    }

    @Test
    void addData_whenNeedLog_thenReturnWithBody() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logBody(true).build();

        ResponseData withBody = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(withBody);
        assertTrue(withBody.getLogMessage().contains("BODY:"));
        assertTrue(withBody.getLogMessage().contains(bodyStr));
    }
}