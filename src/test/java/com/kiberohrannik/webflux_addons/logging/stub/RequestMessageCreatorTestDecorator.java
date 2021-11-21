package com.kiberohrannik.webflux_addons.logging.stub;

import com.kiberohrannik.webflux_addons.logging.request.message.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestMessageCreatorTestDecorator implements RequestMessageCreator {

    private final RequestMessageCreator requestMessageCreator;
    private final LoggingProperties loggingProperties;
    private final String bodyInLogMessage;


    public RequestMessageCreatorTestDecorator(RequestMessageCreator sourceMessageCreator,
                                              LoggingProperties sourceLoggingProperties) {

        this.requestMessageCreator = sourceMessageCreator;
        this.loggingProperties = sourceLoggingProperties;
        this.bodyInLogMessage = null;
    }

    public RequestMessageCreatorTestDecorator(RequestMessageCreator sourceMessageCreator,
                                              LoggingProperties sourceLoggingProperties,
                                              String bodyInLogMessage) {

        this.requestMessageCreator = sourceMessageCreator;
        this.loggingProperties = sourceLoggingProperties;
        this.bodyInLogMessage = bodyInLogMessage;
    }

    @Override
    public Mono<String> formatMessage(ClientRequest request) {
        return requestMessageCreator.formatMessage(request)
                .doOnNext(message -> {
                    assertWithHttpMethodAndUrl(message, request);

                    if (loggingProperties.isLogRequestId()) {
                        assertWithReqId(message, request, loggingProperties);
                    }

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

    private void assertWithReqId(String message, ClientRequest request, LoggingProperties loggingProperties) {
        if (loggingProperties.getRequestIdPrefix() == null) {
            assertTrue(message.contains(request.logPrefix()));
        } else {
            assertTrue(message.contains(loggingProperties.getRequestIdPrefix() + "_" + request.logPrefix()));
        }
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
