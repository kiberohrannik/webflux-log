package com.kv.webflux.logging.client.response.message;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.response.message.formatter.BodyFormatter;
import com.kv.webflux.logging.client.response.message.formatter.CookieClientResponseFormatter;
import com.kv.webflux.logging.client.response.message.formatter.HeaderClientResponseFormatter;
import com.kv.webflux.logging.client.response.message.formatter.ReqIdClientResponseFormatter;
import com.kv.webflux.logging.util.TestUtils;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BaseResponseMessageCreatorUnitTest extends BaseTest {

    private ResponseMessageCreator responseMessageCreator;

    @Spy
    private ReqIdClientResponseFormatter reqIdClientResponseFormatter;

    @Spy
    private HeaderClientResponseFormatter headerClientResponseFormatter;

    @Spy
    private CookieClientResponseFormatter cookieClientResponseFormatter;

    private final LoggingProperties properties = LoggingProperties.builder()
            .logHeaders(true)
            .logCookies(true)
            .logRequestId(true)
            .build();


    @BeforeEach
    void setUp() {
        responseMessageCreator = new BaseResponseMessageCreator(
                properties,
                List.of(reqIdClientResponseFormatter, headerClientResponseFormatter, cookieClientResponseFormatter),
                new BodyFormatter()
        );
    }


    @Test
    void formatMessage_usingInjectedFormatters() {
        ClientResponse testResponse = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Some Auth")
                .cookie("Session", "sid4567")
                .build();

        long exchangeElapsedTimeMillis = new Random().nextInt(999);

        ResponseData result = responseMessageCreator.formatMessage(exchangeElapsedTimeMillis, testResponse).block();

        assertNotNull(result);
        assertNotNull(result.getResponse());

        String actualLogMessage = result.getLogMessage();

        assertTrue(actualLogMessage.contains("RESPONSE:"));
        assertTrue(actualLogMessage.contains("ELAPSED TIME:"));
        assertTrue(actualLogMessage.contains(String.valueOf(exchangeElapsedTimeMillis)));

        assertTrue(actualLogMessage.contains(TestUtils.formatToLoggedReqId(testResponse.logPrefix())));
        assertTrue(actualLogMessage.contains(HttpHeaders.AUTHORIZATION + "=Some Auth"));
        assertTrue(actualLogMessage.contains("Session=sid4567"));

        verify(reqIdClientResponseFormatter).formatMessage(testResponse, properties);
        verify(headerClientResponseFormatter).formatMessage(testResponse, properties);
        verify(cookieClientResponseFormatter).formatMessage(testResponse, properties);
    }
}
