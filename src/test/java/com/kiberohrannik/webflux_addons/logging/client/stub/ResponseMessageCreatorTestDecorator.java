package com.kiberohrannik.webflux_addons.logging.client.stub;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseMessageCreator;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

import static com.kiberohrannik.webflux_addons.logging.client.util.TestUtils.formatToLoggedReqId;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseMessageCreatorTestDecorator implements ResponseMessageCreator {

    private final ResponseMessageCreator responseMessageCreator;
    private final LoggingProperties loggingProperties;

    private String bodyInLogMessage = LoggingUtils.NO_BODY_MESSAGE;


    public ResponseMessageCreatorTestDecorator(ResponseMessageCreator sourceMessageCreator,
                                               LoggingProperties sourceLoggingProperties,
                                               String bodyInLogMessage) {

        this.responseMessageCreator = sourceMessageCreator;
        this.loggingProperties = sourceLoggingProperties;

        if (bodyInLogMessage != null) {
            this.bodyInLogMessage = bodyInLogMessage;
        }
    }

    @Override
    public Mono<ResponseData> formatMessage(Long responseTimeMillis, ClientResponse response) {
        return responseMessageCreator.formatMessage(responseTimeMillis, response)
                .doOnNext(resData -> {
                    assertTrue(resData.getLogMessage().contains(String.valueOf(responseTimeMillis)));

                    if (loggingProperties.isLogRequestId()) {
                        assertWithReqId(resData.getLogMessage(), response, loggingProperties);
                    }

                    if (loggingProperties.isLogHeaders()) {
                        assertWithHeaders(loggingProperties.getMaskedHeaders(), resData.getLogMessage(), response);
                    }

                    if (loggingProperties.isLogCookies()) {
                        assertWithCookies(loggingProperties.getMaskedCookies(), resData.getLogMessage(), response);
                    }

                    if (loggingProperties.isLogBody()) {
                        assertWithBody(resData.getLogMessage());
                    }
                });
    }


    private void assertWithReqId(String message, ClientResponse response, LoggingProperties loggingProps) {
        if (loggingProps.getRequestIdPrefix() == null) {
            assertTrue(message.contains(formatToLoggedReqId(response.logPrefix())));
        } else {
            assertTrue(message.contains(formatToLoggedReqId(response.logPrefix(), loggingProps.getRequestIdPrefix())));
        }
    }

    private void assertWithHeaders(String[] maskedHeaders, String message, ClientResponse response) {
        response.headers()
                .asHttpHeaders()
                .forEach((name, values) -> {

                    if (!name.equals("Set-Cookie")) {
                        assertTrue(message.contains(name));

                        values.forEach(value -> {
                            if (maskedHeaders != null && Arrays.asList(maskedHeaders).contains(name)) {
                                assertFalse(message.contains(value));
                                assertTrue(message.contains(name + "=" + LoggingUtils.DEFAULT_MASK));
                            } else {
                                assertTrue(message.contains(value));
                            }
                        });
                    }
                });
    }

    private void assertWithCookies(String[] maskedCookies, String message, ClientResponse response) {
        response.cookies()
                .forEach((name, values) -> {
                    assertTrue(message.contains(name));

                    values.forEach(resCookie -> {
                        String value = resCookie.toString();

                        if (maskedCookies != null && Arrays.asList(maskedCookies).contains(name)) {
                            assertFalse(message.contains(value));
                            assertTrue(message.contains(name + "=" + LoggingUtils.DEFAULT_MASK));
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
