package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CookieProviderUnitTest extends BaseTest {

    private final CookieProvider provider = new CookieProvider();

    private final LoggingProperties propsNoMasked = LoggingProperties.builder().logCookies(true).build();
    private final String notExistingCookieName = RandomString.make();


    @Test
    void createClientRequestMessage_whenNoMasked_thenAddAll() {
        String cookieName0 = RandomString.make();
        String cookieValue0 = RandomString.make();

        String cookieName1 = RandomString.make();
        String cookieValue1 = RandomString.make();

        String cookieValue2 = RandomString.make();

        MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();
        cookies.add(cookieName0, cookieValue0);
        cookies.add(cookieName1, cookieValue1);
        cookies.add(cookieName0, cookieValue2);

        String actual = provider.createClientRequestMessage(cookies, propsNoMasked);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" COOKIES: [ ")),
                () -> assertTrue(actual.contains(cookieName0 + "=" + cookieValue0)),
                () -> assertTrue(actual.contains(cookieName1 + "=" + cookieValue1)),
                () -> assertTrue(actual.contains(cookieName0 + "=" + cookieValue2))
        );
    }

    @Test
    void createClientRequestMessage_whenMasked_thenAddWithMask() {
        String cookieName0 = RandomString.make();
        String cookieValue0 = RandomString.make();

        String cookieName1 = RandomString.make();
        String cookieValue1 = RandomString.make();

        String cookieName2 = RandomString.make();
        String cookieValue2 = RandomString.make();

        String cookieValue3 = RandomString.make();

        LoggingProperties propsWithMasked = LoggingProperties.builder()
                .logCookies(true)
                .maskedCookies(cookieName0, cookieName1, notExistingCookieName)
                .build();

        MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();
        cookies.add(cookieName0, cookieValue0);
        cookies.add(cookieName1, cookieValue1);
        cookies.add(cookieName2, cookieValue2);
        cookies.add(cookieName0, cookieValue3);

        String actual = provider.createClientRequestMessage(cookies, propsWithMasked);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" COOKIES: [ ")),

                () -> assertFalse(actual.contains(cookieName0 + "=" + cookieValue0)),
                () -> assertEquals(2, StringUtils.countMatches(actual, cookieName0 + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertFalse(actual.contains(cookieName1 + "=" + cookieValue1)),
                () -> assertTrue(actual.contains(cookieName1 + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertFalse(actual.contains(cookieName0 + "=" + cookieValue3)),
                () -> assertTrue(actual.contains(cookieName2 + "=" + cookieValue2)),

                () -> assertFalse(actual.contains(notExistingCookieName))
        );
    }

    @ParameterizedTest
    @MethodSource("getHttpCookies")
    void createServerRequestMessage_whenNoMasked_thenAddAll(HttpCookie cookie0, HttpCookie cookie1,
                                                            HttpCookie cookie2, HttpCookie cookie3,
                                                            MultiValueMap<String, HttpCookie> cookieMap) {

        String actual = provider.createServerRequestMessage(cookieMap, propsNoMasked);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" COOKIES: [ ")),
                () -> assertTrue(actual.contains(cookie0.toString())),
                () -> assertTrue(actual.contains(cookie1.toString())),
                () -> assertTrue(actual.contains(cookie2.toString())),
                () -> assertTrue(actual.contains(cookie3.toString()))
        );
    }

    @ParameterizedTest
    @MethodSource("getHttpCookies")
    void createServerRequestMessage_whenMasked_thenAddWithMask(HttpCookie cookie0, HttpCookie cookie1,
                                                               HttpCookie cookie2, HttpCookie cookie3,
                                                               MultiValueMap<String, HttpCookie> cookieMap) {

        LoggingProperties propsWithMasked = LoggingProperties.builder()
                .logCookies(true)
                .maskedCookies(cookie0.getName(), cookie1.getName(), notExistingCookieName)
                .build();

        String actual = provider.createServerRequestMessage(cookieMap, propsWithMasked);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" COOKIES: [ ")),

                () -> assertFalse(actual.contains(cookie0.toString())),
                () -> assertEquals(2, StringUtils.countMatches(actual, cookie0.getName() + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertFalse(actual.contains(cookie1.toString())),
                () -> assertTrue(actual.contains(cookie1.getName() + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertFalse(actual.contains(cookie2.toString())),
                () -> assertTrue(actual.contains(cookie2.getName() + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertTrue(actual.contains(cookie3.toString())),
                () -> assertFalse(actual.contains(notExistingCookieName))
        );
    }


    @ParameterizedTest
    @MethodSource("getResponseCookies")
    void createResponseMessage_whenNoMasked_thenAddAll(ResponseCookie cookie0, ResponseCookie cookie1,
                                                       ResponseCookie cookie2, ResponseCookie cookie3,
                                                       MultiValueMap<String, ResponseCookie> cookieMap) {

        String actual = provider.createResponseMessage(cookieMap, propsNoMasked);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" COOKIES (Set-Cookie): [ ")),
                () -> assertTrue(actual.contains(cookie0.toString())),
                () -> assertTrue(actual.contains(cookie1.toString())),
                () -> assertTrue(actual.contains(cookie2.toString())),
                () -> assertTrue(actual.contains(cookie3.toString()))
        );
    }

    @ParameterizedTest
    @MethodSource("getResponseCookies")
    void createResponseMessage_whenMasked_thenAddWithMask(ResponseCookie cookie0, ResponseCookie cookie1,
                                                          ResponseCookie cookie2, ResponseCookie cookie3,
                                                          MultiValueMap<String, ResponseCookie> cookieMap) {

        LoggingProperties propsWithMasked = LoggingProperties.builder()
                .logCookies(true)
                .maskedCookies(cookie0.getName(), cookie1.getName(), notExistingCookieName)
                .build();

        String actual = provider.createResponseMessage(cookieMap, propsWithMasked);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" COOKIES (Set-Cookie): [ ")),

                () -> assertFalse(actual.contains(cookie0.getName() + "=" + cookie0.getValue())),
                () -> assertEquals(2, StringUtils.countMatches(actual, cookie0.getName() + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertFalse(actual.contains(cookie1.getName() + "=" + cookie1.getValue())),
                () -> assertTrue(actual.contains(cookie1.getName() + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertFalse(actual.contains(cookie2.getName() + "=" + cookie2.getValue())),
                () -> assertTrue(actual.contains(cookie2.getName() + "=" + LoggingUtils.DEFAULT_MASK)),

                () -> assertTrue(actual.contains(cookie3.toString())),
                () -> assertFalse(actual.contains(notExistingCookieName))
        );
    }


    private static Stream<Arguments> getHttpCookies() {
        HttpCookie cookie0 = new HttpCookie(RandomString.make(), RandomString.make());
        HttpCookie cookie1 = new HttpCookie(RandomString.make(), RandomString.make());
        HttpCookie cookie2 = new HttpCookie(cookie0.getName(), RandomString.make());
        HttpCookie cookie3 = new HttpCookie(RandomString.make(), RandomString.make());

        MultiValueMap<String, HttpCookie> cookieMap = new LinkedMultiValueMap<>();
        cookieMap.add(cookie0.getName(), cookie0);
        cookieMap.add(cookie1.getName(), cookie1);
        cookieMap.add(cookie2.getName(), cookie2);
        cookieMap.add(cookie3.getName(), cookie3);

        return Stream.of(Arguments.of(cookie0, cookie1, cookie2, cookie3, cookieMap));
    }

    private static Stream<Arguments> getResponseCookies() {
        ResponseCookie cookie0 = ResponseCookie.from(RandomString.make(), RandomString.make())
                .httpOnly(true).secure(true).build();

        ResponseCookie cookie1 = ResponseCookie.from(RandomString.make(), RandomString.make())
                .httpOnly(false).domain(RandomString.make()).build();

        ResponseCookie cookie2 = ResponseCookie.from(cookie0.getName(), RandomString.make())
                .httpOnly(true).sameSite(RandomString.make()).build();

        ResponseCookie cookie3 = ResponseCookie.from(RandomString.make(), RandomString.make())
                .httpOnly(true).sameSite(RandomString.make()).build();

        MultiValueMap<String, ResponseCookie> cookieMap = new LinkedMultiValueMap<>();
        cookieMap.add(cookie0.getName(), cookie0);
        cookieMap.add(cookie1.getName(), cookie1);
        cookieMap.add(cookie2.getName(), cookie2);
        cookieMap.add(cookie3.getName(), cookie3);

        return Stream.of(Arguments.of(cookie0, cookie1, cookie2, cookie3, cookieMap));
    }
}
