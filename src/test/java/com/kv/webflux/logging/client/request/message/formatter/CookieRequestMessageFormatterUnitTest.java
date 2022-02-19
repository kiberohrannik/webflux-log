package com.kv.webflux.logging.client.request.message.formatter;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.net.URI;

import static com.kv.webflux.logging.client.LoggingUtils.EMPTY_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

public class CookieRequestMessageFormatterUnitTest extends BaseTest {

    private final CookieClientRequestFormatter formatter = new CookieClientRequestFormatter();

    private final ClientRequest testRequest = ClientRequest.create(HttpMethod.GET, URI.create("/someUri"))
            .cookie("Cookie-1", "some-text-one")
            .cookie("Cookie-1", "some-text-two")
            .cookie("Some2", "any-contentSecond")
            .cookie("Session", "session1234")
            .build();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties properties = LoggingProperties.builder().logCookies(false).build();

        String result = formatter.formatMessage(testRequest, properties);
        assertNotNull(result);
        assertEquals(EMPTY_MESSAGE, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithCookies() {
        LoggingProperties properties = LoggingProperties.builder().logCookies(true).build();

        String withCookies = formatter.formatMessage(testRequest, properties);
        assertNotNull(withCookies);
        assertAll(
                () -> assertTrue(withCookies.contains("COOKIES:")),
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

        String withCookies = formatter.formatMessage(testRequest, properties);
        assertNotNull(withCookies);
        assertAll(
                () -> assertTrue(withCookies.contains("COOKIES:")),
                () -> assertTrue(withCookies.contains("Some2=any-contentSecond")),
                () -> assertTrue(withCookies.contains("Session={masked}")),
                () -> assertFalse(withCookies.contains("AbsentCookie321"))
        );
    }
}