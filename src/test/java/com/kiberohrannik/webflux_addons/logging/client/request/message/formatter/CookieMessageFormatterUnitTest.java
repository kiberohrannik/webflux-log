package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class CookieMessageFormatterUnitTest extends BaseTest {

    private final CookieMessageFormatter formatter = new CookieMessageFormatter();

    private final ClientRequest testRequest = ClientRequest.create(HttpMethod.GET, URI.create("/someUri"))
            .cookie("Cookie-1", "some-text-one")
            .cookie("Cookie-1", "some-text-two")
            .cookie("Some2", "any-contentSecond")
            .cookie("Session", "session1234")
            .build();

    private final String sourceMessage = RandomString.make();


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logCookies(false).build();

        String result = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(result);
        assertEquals(sourceMessage, result);
    }

    @Test
    void addData_whenNeedLog_thenReturnWithCookies() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logCookies(true).build();

        String withCookies = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
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
        LoggingProperties loggingProperties = LoggingProperties.builder()
                .logCookies(true)
                .maskedCookies("Session", "AbsentCookie321")
                .build();

        String withCookies = formatter.addData(testRequest, loggingProperties, Mono.just(sourceMessage)).block();
        assertNotNull(withCookies);
        assertAll(
                () -> assertTrue(withCookies.contains("COOKIES:")),
                () -> assertTrue(withCookies.contains("Some2=any-contentSecond")),
                () -> assertTrue(withCookies.contains("Session={masked}")),
                () -> assertFalse(withCookies.contains("AbsentCookie321"))
        );
    }
}