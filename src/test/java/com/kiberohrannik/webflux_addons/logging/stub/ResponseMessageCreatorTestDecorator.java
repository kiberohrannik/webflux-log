package com.kiberohrannik.webflux_addons.logging.stub;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseMessageCreator;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

import static com.kiberohrannik.webflux_addons.util.TestUtils.formatToLoggedReqId;
import static org.junit.jupiter.api.Assertions.*;

public class ResponseMessageCreatorTestDecorator implements ResponseMessageCreator {

    private final ResponseMessageCreator responseMessageCreator;
    private final LoggingProperties loggingProperties;

    private String bodyInLogMessage = LoggingUtils.NO_BODY_MESSAGE;
    private final long expectedResponseTimeMillis;
    private final long responseTimeDelta = 400;


    public ResponseMessageCreatorTestDecorator(ResponseMessageCreator sourceMessageCreator,
                                               LoggingProperties sourceLoggingProperties,
                                               String bodyInLogMessage,
                                               long expectedResponseTimeMillis) {

        this.responseMessageCreator = sourceMessageCreator;
        this.loggingProperties = sourceLoggingProperties;
        this.expectedResponseTimeMillis = expectedResponseTimeMillis;

        if (bodyInLogMessage != null) {
            this.bodyInLogMessage = bodyInLogMessage;
        }
    }

    @Override
    public Mono<ResponseData> formatMessage(Long responseTimeMillis, ClientResponse response) {
        return responseMessageCreator.formatMessage(responseTimeMillis, response)
                .doOnNext(resData -> {
                    assertWithExchangeTime(responseTimeMillis, resData.getLogMessage(), response);

//                    if (loggingProperties.isLogRequestId()) {
//                        assertWithReqId(resData.getLogMessage(), response, loggingProperties);
//                    }

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


    private void assertWithExchangeTime(Long responseTimeMillis, String message, ClientResponse response) {
        String timeInMessage = message.replaceAll(".*ELAPSED TIME: ", "")
                .replaceAll("\\s.*", "")
                .replaceAll("\\D", "");

        long elapsedTimeInLog = Long.parseLong(timeInMessage);

        assertAll(
                () -> assertTrue(message.contains(String.valueOf(responseTimeMillis))),
                () -> assertTrue(elapsedTimeInLog >= expectedResponseTimeMillis),
                () -> assertTrue(elapsedTimeInLog <= expectedResponseTimeMillis + responseTimeDelta)
        );
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