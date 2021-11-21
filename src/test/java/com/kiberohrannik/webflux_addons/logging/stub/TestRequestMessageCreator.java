package com.kiberohrannik.webflux_addons.logging.stub;

import com.kiberohrannik.webflux_addons.logging.creator.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.filter.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRequestMessageCreator extends RequestMessageCreator {

    private final String bodyInLogMessage;

    public TestRequestMessageCreator() {
        super();
        this.bodyInLogMessage = null;
    }

    public TestRequestMessageCreator(String bodyInLogMessage) {
        super();
        this.bodyInLogMessage = bodyInLogMessage;
    }


    @Override
    public Mono<String> formatMessage(ClientRequest request, LoggingProperties loggingProperties) {
        return super.formatMessage(request, loggingProperties)
                .doOnNext(message -> {
                    assertWithHttpMethodAndUrl(message, request);

                    if (loggingProperties.isLogHeaders()) {
                        assertWithHeaders(message, request);
                    }

                    if (loggingProperties.isLogCookies()) {
                        assertWithCookies(message, request);
                    }

                    if (loggingProperties.isLogBody()) {
                        assertWithBody(message);
                    }
                });
    }

    private void assertWithHttpMethodAndUrl(String message, ClientRequest request) {
        assertAll(
                () -> assertTrue(message.contains(request.method().name())),
                () -> assertTrue(message.contains(request.url().toString()))
        );
    }

    private void assertWithHeaders(String message, ClientRequest request) {
        request.headers()
                .forEach((name, values) -> {
                    assertTrue(message.contains(name));
                    values.forEach(value -> assertTrue(message.contains(value)));
                });
    }

    private void assertWithCookies(String message, ClientRequest request) {
        request.cookies()
                .forEach((name, values) -> {
                    assertTrue(message.contains(name));
                    values.forEach(value -> assertTrue(message.contains(value)));
                });
    }

    private void assertWithBody(String message) {
        Objects.requireNonNull(bodyInLogMessage);
        assertTrue(message.contains(bodyInLogMessage));
    }
}
