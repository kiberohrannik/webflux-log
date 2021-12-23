package com.kiberohrannik.webflux_addons.logging.client.response.message;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.CookieMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.HeaderMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.ReqIdMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.client.util.TestUtils;
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
    private ReqIdMessageFormatter reqIdFormatter;

    @Spy
    private HeaderMessageFormatter headerFormatter;

    @Spy
    private CookieMessageFormatter cookieFormatter;

    private final LoggingProperties loggingProperties = LoggingProperties.builder()
            .logHeaders(true)
            .logCookies(true)
            .logRequestId(true)
            .build();


    @BeforeEach
    void setUp() {
        responseMessageCreator = new BaseResponseMessageCreator(loggingProperties,
                List.of(reqIdFormatter, headerFormatter, cookieFormatter));
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
        System.out.println(actualLogMessage);

        assertTrue(actualLogMessage.contains("RESPONSE:"));
        assertTrue(actualLogMessage.contains("ELAPSED TIME:"));
        assertTrue(actualLogMessage.contains(String.valueOf(exchangeElapsedTimeMillis)));

        assertTrue(actualLogMessage.contains(TestUtils.formatToLoggedReqId(testResponse.logPrefix())));
        assertTrue(actualLogMessage.contains(HttpHeaders.AUTHORIZATION + "=Some Auth"));
        assertTrue(actualLogMessage.contains("Session=sid4567"));

        verify(reqIdFormatter).addData(eq(loggingProperties), any());
        verify(headerFormatter).addData(eq(loggingProperties), any());
        verify(cookieFormatter).addData(eq(loggingProperties), any());
    }
}
