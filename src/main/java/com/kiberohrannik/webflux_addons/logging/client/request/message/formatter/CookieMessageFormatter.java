package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.List;


public class CookieMessageFormatter implements RequestDataMessageFormatter {

    @Override
    public Mono<String> addData(ClientRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogCookies()) {
            return sourceMessage.map(source -> source.concat(extractCookies(request, loggingProperties)));
        }

        return sourceMessage;
    }


    private String extractCookies(ClientRequest request, LoggingProperties props) {
        StringBuilder sb = new StringBuilder("\nCOOKIES: [ ");

        if (props.getMaskedCookies() == null) {
            extractAll(request.cookies(), sb);
        } else {
            extractAll(setMask(request.cookies(), props.getMaskedCookies()), sb);
        }

        return sb.append("]").toString();
    }

    private MultiValueMap<String, String> setMask(MultiValueMap<String, String> cookies, String[] cookiesToMask) {
        MultiValueMap<String, String> cookiesToLog = new LinkedMultiValueMap<>(cookies);

        for (String maskedCookieName : cookiesToMask) {
            if (cookiesToLog.getFirst(maskedCookieName) != null) {
                cookiesToLog.put(maskedCookieName, List.of(LoggingUtils.DEFAULT_MASK));
            }
        }

        return cookiesToLog;
    }

    private void extractAll(MultiValueMap<String, String> cookies, StringBuilder sb) {
        cookies.forEach((cookieName, cookieValues) -> cookieValues
                .forEach(value -> sb.append(cookieName).append("=").append(value).append(" ")));
    }
}