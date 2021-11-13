package com.kiberohrannik.webflux_addons.logging.creator;

import com.kiberohrannik.webflux_addons.logging.filter.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class RequestMessageCreator {

    private final RequestBodyExtractor bodyExtractor = new RequestBodyExtractor();


    public Mono<String> formatMessage(ClientRequest request, LoggingProperties loggingProperties) {
        StringBuilder messageBuilder = new StringBuilder("REQUEST: ");
        messageBuilder.append(request.method().name()).append(" ").append(request.url());

        if (loggingProperties.isLogHeaders()) {
            addHeaders(messageBuilder, request);
        }
        if (loggingProperties.isLogCookies()) {
            addCookies(messageBuilder, request);
        }

        if (loggingProperties.isLogBody()) {
            return addBody(messageBuilder, request)
                    .map(StringBuilder::toString);
        }

        return Mono.just(messageBuilder.toString());
    }


    private void addHeaders(StringBuilder messageBuilder, ClientRequest request) {
        messageBuilder.append(" \nHEADERS: [ ");

        request.headers()
                .forEach((headerName, headerValues) -> headerValues
                        .forEach(value -> messageBuilder.append(headerName).append("=").append(value).append(" ")));

        messageBuilder.append("]");
    }

    private void addCookies(StringBuilder messageBuilder, ClientRequest request) {
        messageBuilder.append(" \nCOOKIES: [ ");

        request.cookies()
                .forEach((cookieName, cookieValues) -> cookieValues
                        .forEach(value -> messageBuilder.append(cookieName).append("=").append(value).append(" ")));

        messageBuilder.append("]");
    }

    private Mono<StringBuilder> addBody(StringBuilder messageBuilder, ClientRequest request) {
        return bodyExtractor.extractBody(request)
                .map(bodyStr -> messageBuilder.append(" \nBODY: [ ").append(bodyStr).append(" ]"));
    }
}
