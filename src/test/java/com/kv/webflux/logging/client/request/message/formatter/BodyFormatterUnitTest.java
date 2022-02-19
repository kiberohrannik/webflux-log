package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.kv.webflux.logging.client.LoggingUtils.EMPTY_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

public class BodyFormatterUnitTest extends BaseTest {

    private final BodyClientRequestFormatter formatter = new BodyClientRequestFormatter();

    private final String bodyStr = RandomString.make();

    private final ClientRequest requestWithBody = ClientRequest.create(HttpMethod.POST, URI.create("/someUri"))
            .body(Mono.just(bodyStr), String.class)
            .build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties properties = LoggingProperties.builder().logBody(false).build();

        String result = formatter.formatMessage(requestWithBody, properties).block();
        assertNotNull(result);
        assertEquals(EMPTY_MESSAGE, result);
    }

    @Test
    void addData_whenNoBody_thenAddEmpty() {
        LoggingProperties properties = LoggingProperties.builder().logBody(true).build();
        ClientRequest requestNoBody = ClientRequest.create(HttpMethod.POST, URI.create("/someUri")).build();

        String result = formatter.formatMessage(requestNoBody, properties).block();
        assertNotNull(result);
        assertTrue(result.contains("BODY:"));
        assertTrue(result.contains(LoggingUtils.NO_BODY_MESSAGE));
    }

    @Test
    void addData_whenNeedLog_thenReturnWithBody() {
        LoggingProperties properties = LoggingProperties.builder().logBody(true).build();

        String withBody = formatter.formatMessage(requestWithBody, properties).block();
        assertNotNull(withBody);
        assertTrue(withBody.contains("BODY:"));
        assertTrue(withBody.contains(bodyStr));
    }
}