package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class CookieProvider {

    public String createClientRequestMessage(MultiValueMap<String, String> cookies, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" COOKIES: [ ");

        if (props.getMaskedCookies() == null) {
            extractInClientRequest(cookies, sb);
        } else {
            extractInClientRequest(setMaskInClientRequest(cookies, props.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }

    public String createServerRequestMessage(MultiValueMap<String, HttpCookie> cookies, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" COOKIES: [ ");

        if (props.getMaskedCookies() == null) {
            extractInServerRequest(cookies, sb);
        } else {
            extractInServerRequest(setMaskInServerRequest(cookies, props.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }

    public String createResponseMessage(MultiValueMap<String, ResponseCookie> cookies, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" COOKIES (Set-Cookie): [ ");

        if (props.getMaskedCookies() == null) {
            extractInResponse(cookies, sb);
        } else {
            extractInResponse(setMaskInResponse(cookies, props.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }


    private MultiValueMap<String, String> setMaskInClientRequest(MultiValueMap<String, String> cookies,
                                                                 String[] cookiesToMask) {
        return ProviderUtils.setMaskToValues(cookies, cookiesToMask, LoggingUtils.DEFAULT_MASK);
    }

    private MultiValueMap<String, HttpCookie> setMaskInServerRequest(MultiValueMap<String, HttpCookie> cookies,
                                                                     String[] cookiesToMask) {

        MultiValueMap<String, HttpCookie> copiedMap = new LinkedMultiValueMap<>(cookies);

        for (String maskedName : cookiesToMask) {
            ProviderUtils.setMaskToValue(copiedMap, maskedName, new HttpCookie(maskedName, LoggingUtils.DEFAULT_MASK));
        }
        return copiedMap;
    }

    private MultiValueMap<String, ResponseCookie> setMaskInResponse(MultiValueMap<String, ResponseCookie> cookies,
                                                                    String[] cookiesToMask) {

        MultiValueMap<String, ResponseCookie> copiedMap = new LinkedMultiValueMap<>(cookies);

        for (String maskedName : cookiesToMask) {
            ResponseCookie mask = ResponseCookie.from(maskedName, LoggingUtils.DEFAULT_MASK).build();
            ProviderUtils.setMaskToValue(copiedMap, maskedName, mask);
        }
        return copiedMap;
    }

    private void extractInClientRequest(MultiValueMap<String, String> cookies, StringBuilder sb) {
        cookies.forEach((name, values) -> values
                .forEach(value -> sb.append(name).append("=").append(value).append(" ")));
    }

    private void extractInServerRequest(MultiValueMap<String, HttpCookie> cookies, StringBuilder sb) {
        cookies.forEach((name, values) -> values
                .forEach(httpCookie -> sb.append(httpCookie).append(" ")));
    }

    private void extractInResponse(MultiValueMap<String, ResponseCookie> cookies, StringBuilder sb) {
        cookies.forEach((name, values) -> values
                .forEach(responseCookie ->
                        sb.append(" [").append(responseCookie).append("]").append(" ")));
    }
}
