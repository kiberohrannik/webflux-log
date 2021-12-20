package com.kiberohrannik.webflux_addons.logging.stub;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.client.request.message.RequestMessageCreator;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

import static com.kiberohrannik.webflux_addons.util.TestUtils.formatToLoggedReqId;
import static org.junit.jupiter.api.Assertions.*;

public class RequestMessageCreatorTestDecorator implements RequestMessageCreator {

    private final RequestMessageCreator requestMessageCreator;
    private final LoggingProperties loggingProperties;
    private String bodyInLogMessage = LoggingUtils.NO_BODY_MESSAGE;


    public RequestMessageCreatorTestDecorator(RequestMessageCreator sourceMessageCreator,
                                              LoggingProperties sourceLoggingProperties,
                                              String bodyInLogMessage) {

        this.requestMessageCreator = sourceMessageCreator;
        this.loggingProperties = sourceLoggingProperties;

        if (bodyInLogMessage != null) {
            this.bodyInLogMessage = bodyInLogMessage;
        }
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
                        assertWithHeaders(loggingProperties.getMaskedHeaders(), message, request);
                    }

                    if (loggingProperties.isLogCookies()) {
                        assertWithCookies(loggingProperties.getMaskedCookies(), message, request);
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

    private void assertWithReqId(String message, ClientRequest request, LoggingProperties loggingProps) {
        if (loggingProps.getRequestIdPrefix() == null) {
            assertTrue(message.contains(formatToLoggedReqId(request.logPrefix())));
        } else {
            assertTrue(message.contains(formatToLoggedReqId(request.logPrefix(), loggingProps.getRequestIdPrefix())));
        }
    }

    private void assertWithHeaders(String[] maskedHeaders, String message, ClientRequest request) {
        request.headers()
                .forEach((name, values) -> {
                    assertTrue(message.contains(name));

                    values.forEach(value -> {
                        if (maskedHeaders != null && Arrays.asList(maskedHeaders).contains(name)) {
                            assertFalse(message.contains(value));
                            assertTrue(message.contains(name + "={masked}"));
                        } else {
                            assertTrue(message.contains(value));
                        }
                    });
                });
    }

    private void assertWithCookies(String[] maskedCookies, String message, ClientRequest request) {
        request.cookies()
                .forEach((name, values) -> {
                    assertTrue(message.contains(name));

                    values.forEach(value -> {
                        if (maskedCookies != null && Arrays.asList(maskedCookies).contains(name)) {
                            assertFalse(message.contains(value));
                            assertTrue(message.contains(name + "={masked}"));
                        } else {
                            assertTrue(message.contains(value));
                        }
                    });
                });
    }

    private void assertWithBody(String message) {
        Objects.requireNonNull(bodyInLogMessage);
        assertTrue(message.contains(bodyInLogMessage));
    }
}
