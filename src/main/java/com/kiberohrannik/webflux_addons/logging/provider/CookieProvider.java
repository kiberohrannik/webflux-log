package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

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

        if (props.getMaskedCookies() != null) {
            setMaskInServerRequest(cookies, props.getMaskedCookies());
        }
        extractInServerRequest(cookies, sb);

        return sb.append("]").toString();
    }

    public String createResponseMessage(MultiValueMap<String, ResponseCookie> cookies, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" COOKIES (Set-Cookie): [ ");

        if (props.getMaskedCookies() == null) {
            this.extractInResponse(cookies, sb);
        } else {
            this.extractInResponse(setMaskInResponse(cookies, props.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }


    private MultiValueMap<String, String> setMaskInClientRequest(MultiValueMap<String, String> cookies,
                                                                 String[] cookiesToMask) {

        MultiValueMap<String, String> cookiesToLog = new LinkedMultiValueMap<>(cookies);
        for (String maskedName : cookiesToMask) {
            if (cookiesToLog.getFirst(maskedName) != null) {
                cookiesToLog.put(maskedName, List.of(LoggingUtils.DEFAULT_MASK));
            }
        }

        return cookiesToLog;
    }

    private void setMaskInServerRequest(MultiValueMap<String, HttpCookie> cookies, String[] cookiesToMask) {
        MultiValueMap<String, HttpCookie> cookiesToLog = new LinkedMultiValueMap<>(cookies);

        for (String masked : cookiesToMask) {
            if (cookiesToLog.getFirst(masked) != null) {
                cookiesToLog.put(masked, List.of(new HttpCookie(masked, LoggingUtils.DEFAULT_MASK)));
            }
        }
    }

    private MultiValueMap<String, ResponseCookie> setMaskInResponse(MultiValueMap<String, ResponseCookie> cookies,
                                                                    String[] cookiesToMask) {

        MultiValueMap<String, ResponseCookie> cookiesToLog = new LinkedMultiValueMap<>(cookies);
        for (String maskedName : cookiesToMask) {
            if (cookiesToLog.getFirst(maskedName) != null) {
                ResponseCookie masked = ResponseCookie.from(maskedName, LoggingUtils.DEFAULT_MASK).build();
                cookiesToLog.put(maskedName, List.of(masked));
            }
        }

        return cookiesToLog;
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
                .forEach(responseCookie -> sb.append(" [").append(responseCookie).append("]").append(" ")));
    }
}
