package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.kiberohrannik.webflux_addons.logging.client.LoggingUtils.NO_BODY_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

public class BodyMessageFormatterUnitTest extends BaseTest {

    private final BodyMessageFormatter formatter = new BodyMessageFormatter();

    private final String sourceMessage = RandomString.make();
    private final String bodyStr = RandomString.make();

    private final ClientRequest requestWithBody = ClientRequest.create(HttpMethod.POST, URI.create("/someUri"))
            .body(Mono.just(bodyStr), String.class)
            .build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logBody(false).build();

        String result = formatter.addData(requestWithBody, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(result);
        assertEquals(sourceMessage, result);
    }

    @Test
    void addData_whenNoBody_thenAddEmpty() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logBody(true).build();
        ClientRequest requestNoBody = ClientRequest.create(HttpMethod.POST, URI.create("/someUri")).build();

        String result = formatter.addData(requestNoBody, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(result);
        assertTrue(result.contains("BODY:"));
        assertTrue(result.contains(NO_BODY_MESSAGE));
    }

    @Test
    void addData_whenNeedLog_thenReturnWithBody() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logBody(true).build();

        String withBody = formatter.addData(requestWithBody, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(withBody);
        assertTrue(withBody.contains("BODY:"));
        assertTrue(withBody.contains(bodyStr));
    }
}