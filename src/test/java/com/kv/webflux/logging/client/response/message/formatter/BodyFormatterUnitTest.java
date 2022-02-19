package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import com.kv.webflux.logging.client.response.message.ResponseData;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import static org.junit.jupiter.api.Assertions.*;

public class BodyFormatterUnitTest extends BaseTest {

    private final BodyClientResponseFormatter formatter = new BodyClientResponseFormatter();

    private final String bodyStr = RandomString.make();
    private final ClientResponse response = ClientResponse.create(HttpStatus.BAD_REQUEST).body(bodyStr).build();

    private final LoggingProperties propertiesWithBody = LoggingProperties.builder().logBody(true).build();
    private final LoggingProperties propertiesNoBody = LoggingProperties.builder().logBody(false).build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        ResponseData result = formatter.formatMessage(response, propertiesNoBody).block();
        assertNotNull(result);
        assertEquals(LoggingUtils.EMPTY_MESSAGE, result.getLogMessage());
        assertSame(response, result.getResponse());
    }

    @Test
    void addData_whenNoBody_thenAddEmpty() {
        ClientResponse responseWithNoBody = ClientResponse.create(HttpStatus.BAD_REQUEST).build();

        ResponseData result = formatter.formatMessage(responseWithNoBody, propertiesWithBody).block();
        assertNotNull(result);
        assertTrue(result.getLogMessage().contains("BODY:"));
        assertTrue(result.getLogMessage().contains(LoggingUtils.NO_BODY_MESSAGE));
        assertNotSame(response, result.getResponse());
    }

    @Test
    void addData_whenNeedLog_thenReturnWithBody() {
        ResponseData withBody = formatter.formatMessage(response, propertiesWithBody).block();
        assertNotNull(withBody);
        assertTrue(withBody.getLogMessage().contains("BODY:"));
        assertTrue(withBody.getLogMessage().contains(bodyStr));
        assertNotSame(response, withBody.getResponse());
    }
}