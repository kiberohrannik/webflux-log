package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public final class CookieProvider {


    public String createClientRequestMessage(MultiValueMap<String, String> cookies, LoggingProperties properties) {
        if (!properties.isLogCookies()) {
            return LoggingUtils.EMPTY_MESSAGE;
        }

        StringBuilder sb = new StringBuilder(" COOKIES: [ ");

        if (properties.getMaskedCookies() == null) {
            extractInClientRequest(cookies, sb);
        } else {
            extractInClientRequest(setMaskInClientRequest(cookies, properties.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }

    public String createServerRequestMessage(MultiValueMap<String, HttpCookie> cookies, LoggingProperties properties) {
        if (!properties.isLogCookies()) {
            return LoggingUtils.EMPTY_MESSAGE;
        }

        StringBuilder sb = new StringBuilder(" COOKIES: [ ");

        if (properties.getMaskedCookies() == null) {
            extractInServerRequest(cookies, sb);
        } else {
            extractInServerRequest(setMaskInServerRequest(cookies, properties.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }

    public String createResponseMessage(MultiValueMap<String, ResponseCookie> cookies, LoggingProperties properties) {
        if (!properties.isLogCookies()) {
            return LoggingUtils.EMPTY_MESSAGE;
        }

        StringBuilder sb = new StringBuilder(" COOKIES (Set-Cookie): [ ");

        if (properties.getMaskedCookies() == null) {
            extractInResponse(cookies, sb);
        } else {
            extractInResponse(setMaskInResponse(cookies, properties.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }


    private Map<String, List<String>> setMaskInClientRequest(Map<String, List<String>> cookies,
                                                             String[] cookiesToMask) {

        LinkedCaseInsensitiveMap<List<String>> cookiesToLog = new LinkedCaseInsensitiveMap<>();
        cookiesToLog.putAll(cookies);

        return ProviderUtils.setMaskToValues(cookiesToLog, cookiesToMask, LoggingUtils.DEFAULT_MASK);
    }

    private Map<String, List<HttpCookie>> setMaskInServerRequest(Map<String, List<HttpCookie>> cookies,
                                                                 String[] cookiesToMask) {

        LinkedCaseInsensitiveMap<List<HttpCookie>> cookiesToLog = new LinkedCaseInsensitiveMap<>();
        cookiesToLog.putAll(cookies);

        for (String name : cookiesToMask) {
            ProviderUtils.setMaskToValue(cookiesToLog, name, new HttpCookie(name, LoggingUtils.DEFAULT_MASK));
        }

        return cookiesToLog;
    }

    private Map<String, List<ResponseCookie>> setMaskInResponse(MultiValueMap<String, ResponseCookie> cookies,
                                                                String[] cookiesToMask) {

        LinkedCaseInsensitiveMap<List<ResponseCookie>> cookiesToLog = new LinkedCaseInsensitiveMap<>();
        cookiesToLog.putAll(cookies);

        for (String maskedName : cookiesToMask) {
            ResponseCookie mask = ResponseCookie.from(maskedName, LoggingUtils.DEFAULT_MASK).build();
            ProviderUtils.setMaskToValue(cookiesToLog, maskedName, mask);
        }

        return cookiesToLog;
    }

    private void extractInClientRequest(Map<String, List<String>> cookies, StringBuilder sb) {
        cookies.forEach((name, values) -> values
                .forEach(value -> sb.append(name).append("=").append(value).append(" ")));
    }

    private void extractInServerRequest(Map<String, List<HttpCookie>> cookies, StringBuilder sb) {
        cookies.forEach((name, values) -> values
                .forEach(httpCookie -> sb.append(httpCookie).append(" ")));
    }

    private void extractInResponse(Map<String, List<ResponseCookie>> cookies, StringBuilder sb) {
        cookies.forEach((name, values) -> values
                .forEach(responseCookie ->
                        sb.append(" [").append(responseCookie).append("]").append(" ")));
    }
}
