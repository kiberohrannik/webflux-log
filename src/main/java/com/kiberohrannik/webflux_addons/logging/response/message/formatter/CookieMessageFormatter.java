package com.kiberohrannik.webflux_addons.logging.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseData;
import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;


public class CookieMessageFormatter implements ResponseDataMessageFormatter {

    @Override
    public Mono<ResponseData> addData(LoggingProperties loggingProperties,
                                      Mono<ResponseData> sourceMessage) {

        if (loggingProperties.isLogCookies()) {
            return sourceMessage.map(source -> {
                String cookiesMessage = extractCookies(source.getResponse(), loggingProperties);
                return source.addToLogs(cookiesMessage);
            });
        }

        return sourceMessage;
    }


    private String extractCookies(ClientResponse response, LoggingProperties props) {
        StringBuilder sb = new StringBuilder("\nCOOKIES: [ ");

        if (props.getMaskedCookies() == null) {
            extractAll(response.cookies(), sb);
        } else {
            extractAll(setMask(response.cookies(), props.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }

    private MultiValueMap<String, ResponseCookie> setMask(MultiValueMap<String, ResponseCookie> cookies,
                                                          String[] cookiesToMask) {

        MultiValueMap<String, ResponseCookie> cookiesToLog = new LinkedMultiValueMap<>(cookies);
        for (String sensitiveCookieName : cookiesToMask) {
            ResponseCookie maskedCookie = ResponseCookie.from(sensitiveCookieName, LoggingUtils.DEFAULT_MASK).build();
            cookiesToLog.put(sensitiveCookieName, List.of(maskedCookie));
        }

        return cookiesToLog;
    }

    private void extractAll(MultiValueMap<String, ResponseCookie> cookies, StringBuilder sb) {
        cookies.forEach((cookieName, cookieValues) -> cookieValues
                .forEach(value -> sb.append(cookieName).append("=").append(value.getValue()).append(" ")));
    }
}