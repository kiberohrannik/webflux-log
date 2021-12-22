package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.server.RequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public final class CookieMessageFormatter implements ServerMessageFormatter {

    @Override
    public Mono<RequestData> addData(LoggingProperties loggingProperties, Mono<RequestData> sourceMessage) {
        return null;
    }

//    @Override
//    public Mono<String> addData(ServerWebExchange exchange,
//                                LoggingProperties loggingProperties,
//                                Mono<String> sourceMessage) {
//
//        if (loggingProperties.isLogCookies()) {
//            return sourceMessage.map(source ->
//                    source.concat(formatCookieMessage(exchange.getRequest(), loggingProperties)));
//        }
//
//        return sourceMessage;
//    }
//
//
//    private String formatCookieMessage(ServerHttpRequest request, LoggingProperties props) {
//        StringBuilder sb = new StringBuilder(" COOKIES: [ ");
//
//        if (props.getMaskedCookies() != null) {
//            setMask(request.getCookies(), props.getMaskedCookies());
//        }
//        extractAll(request.getCookies(), sb);
//
//        return sb.append("]").toString();
//    }
//
//    private void setMask(MultiValueMap<String, HttpCookie> cookies, String[] cookiesToMask) {
//        MultiValueMap<String, HttpCookie> cookiesToLog = new LinkedMultiValueMap<>(cookies);
//
//        for (String maskedCookie : cookiesToMask) {
//            if (cookiesToLog.getFirst(maskedCookie) != null) {
//                cookiesToLog.put(maskedCookie, List.of(new HttpCookie(maskedCookie, LoggingUtils.DEFAULT_MASK)));
//            }
//        }
//    }
//
//    private void extractAll(MultiValueMap<String, HttpCookie> cookies, StringBuilder sb) {
//        cookies.forEach((cookieName, cookieValues) -> cookieValues
//                .forEach(httpCookie -> sb.append(httpCookie).append(" ")));
//    }
}