package com.kiberohrannik.webflux_addons.logging.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class CookieMessageFormatter implements RequestDataMessageFormatter {

    @Override
    public Mono<String> addData(ClientRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogCookies()) {
            return sourceMessage.map(source -> source.concat(extractCookies(request)));
        }

        return sourceMessage;
    }


    private String extractCookies(ClientRequest request) {
        StringBuilder sb = new StringBuilder("\nCOOKIES: [ ");

        request.cookies()
                .forEach((headerName, headerValues) -> headerValues
                        .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));

        return sb.append("]").toString();
    }
}