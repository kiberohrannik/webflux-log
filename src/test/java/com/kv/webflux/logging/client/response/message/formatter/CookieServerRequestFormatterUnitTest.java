package com.kv.webflux.logging.client.response.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import static org.junit.jupiter.api.Assertions.*;

public class CookieServerRequestFormatterUnitTest extends BaseTest {

    private final CookieClientResponseFormatter formatter = new CookieClientResponseFormatter();

    private final ClientResponse response = ClientResponse.create(HttpStatus.OK)
            .cookie("Cookie-1", "some-text-one")
            .cookie("Cookie-1", "some-text-two")
            .cookie("Some2", "any-contentSecond")
            .cookie("Session", "session1234")
            .build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties properties = LoggingProperties.builder().logCookies(false).build();

        String result = formatter.formatMessage(response, properties);
        assertEquals(LoggingUtils.EMPTY_MESSAGE, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithCookies() {
        LoggingProperties properties = LoggingProperties.builder().logCookies(true).build();

        String withCookies = formatter.formatMessage(response, properties);
        assertAll(
                () -> assertNotNull(withCookies),
                () -> assertTrue(withCookies.contains("COOKIES (Set-Cookie):")),
                () -> assertTrue(withCookies.contains("Cookie-1=some-text-one")),
                () -> assertTrue(withCookies.contains("Cookie-1=some-text-two")),
                () -> assertTrue(withCookies.contains("Some2=any-contentSecond")),
                () -> assertTrue(withCookies.contains("Session=session1234"))
        );
    }

    @Test
    void addData_whenLogAndMaskCookies_thenReturnWithMaskedCookies() {
        LoggingProperties properties = LoggingProperties.builder()
                .logCookies(true)
                .maskedCookies("Session", "AbsentCookie321")
                .build();

        String withCookies = formatter.formatMessage(response, properties);
        assertAll(
                () -> assertNotNull(withCookies),
                () -> assertTrue(withCookies.contains("COOKIES (Set-Cookie):")),
                () -> assertTrue(withCookies.contains("Some2=any-contentSecond")),
                () -> assertTrue(withCookies.contains("Session={masked}")),
                () -> assertFalse(withCookies.contains("AbsentCookie321"))
        );
    }
}