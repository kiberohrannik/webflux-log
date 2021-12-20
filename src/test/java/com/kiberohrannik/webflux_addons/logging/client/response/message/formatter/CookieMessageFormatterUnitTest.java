package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

public class CookieMessageFormatterUnitTest extends BaseTest {

    private final CookieMessageFormatter formatter = new CookieMessageFormatter();

    private final ClientResponse response = ClientResponse.create(HttpStatus.OK)
            .cookie("Cookie-1", "some-text-one")
            .cookie("Cookie-1", "some-text-two")
            .cookie("Some2", "any-contentSecond")
            .cookie("Session", "session1234")
            .build();

    private final String sourceLogMessage = RandomString.make();
    private final ResponseData sourceResponseData = new ResponseData(response, sourceLogMessage);


    @Test
    void addData_whenDontNeedToLog_thenReturnSourceMessage() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logCookies(false).build();

        ResponseData result = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertEquals(sourceLogMessage, result.getLogMessage());
    }

    @Test
    void addData_whenNeedLog_thenReturnWithCookies() {
        LoggingProperties loggingProperties = LoggingProperties.builder().logCookies(true).build();

        ResponseData withCookies = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(withCookies);

        String actualLogMessage = withCookies.getLogMessage();
        assertAll(
                () -> assertTrue(actualLogMessage.contains("COOKIES (Set-Cookie):")),
                () -> assertTrue(actualLogMessage.contains("Cookie-1=some-text-one")),
                () -> assertTrue(actualLogMessage.contains("Cookie-1=some-text-two")),
                () -> assertTrue(actualLogMessage.contains("Some2=any-contentSecond")),
                () -> assertTrue(actualLogMessage.contains("Session=session1234"))
        );
    }

    @Test
    void addData_whenLogAndMaskCookies_thenReturnWithMaskedCookies() {
        LoggingProperties loggingProperties = LoggingProperties.builder()
                .logCookies(true)
                .maskedCookies("Session", "AbsentCookie321")
                .build();

        ResponseData withCookies = formatter.addData(loggingProperties, Mono.just(sourceResponseData)).block();
        assertNotNull(withCookies);

        String actualLogMessage = withCookies.getLogMessage();
        assertAll(
                () -> assertTrue(actualLogMessage.contains("COOKIES (Set-Cookie):")),
                () -> assertTrue(actualLogMessage.contains("Some2=any-contentSecond")),
                () -> assertTrue(actualLogMessage.contains("Session={masked}")),
                () -> assertFalse(actualLogMessage.contains("AbsentCookie321"))
        );
    }
}