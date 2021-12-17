package com.kiberohrannik.webflux_addons.logging.response.message;

import com.kiberohrannik.webflux_addons.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.CookieMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.HeaderMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.ReqIdMessageFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BaseResponseMessageCreatorUnitTest extends BaseTest {

    private ResponseMessageCreator responseMessageCreator;

    @Spy
    private ReqIdMessageFormatter reqIdMessageFormatter;

    @Spy
    private HeaderMessageFormatter headerMessageFormatter;

    @Spy
    private CookieMessageFormatter cookieMessageFormatter;

    private final LoggingProperties loggingProperties = LoggingProperties.builder()
            .logHeaders(true)
            .logCookies(true)
            .logRequestId(true)
            .build();


    @BeforeEach
    void setUp() {
        responseMessageCreator = new BaseResponseMessageCreator(loggingProperties,
                List.of(reqIdMessageFormatter, headerMessageFormatter, cookieMessageFormatter));
    }


    @Test
    void formatMessage_usingInjectedFormatters() {
        ClientResponse testResponse = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Some Auth")
                .cookie("Session", "sid4567")
                .build();

        long exchangeElapsedTimeMillis = new Random().nextInt();

        ResponseData result = responseMessageCreator.formatMessage(exchangeElapsedTimeMillis, testResponse).block();

        assertNotNull(result);
        assertNotNull(result.getResponse());

        String actualLogMessage = result.getLogMessage();
        assertTrue(actualLogMessage.contains("RESPONSE:"));
        assertTrue(actualLogMessage.contains("ELAPSED TIME:"));
        assertTrue(actualLogMessage.contains(String.valueOf(exchangeElapsedTimeMillis)));

        assertTrue(actualLogMessage.contains(testResponse.logPrefix().replaceAll("[\\[\\]\\s]", "")));
        assertTrue(actualLogMessage.contains(HttpHeaders.AUTHORIZATION + "=Some Auth"));
        assertTrue(actualLogMessage.contains("Session=sid4567"));

        verify(reqIdMessageFormatter).addData(eq(loggingProperties), any());
        verify(headerMessageFormatter).addData(eq(loggingProperties), any());
        verify(cookieMessageFormatter).addData(eq(loggingProperties), any());
    }
}