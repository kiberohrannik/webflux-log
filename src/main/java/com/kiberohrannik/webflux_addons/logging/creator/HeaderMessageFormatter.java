package com.kiberohrannik.webflux_addons.logging.creator;

import com.kiberohrannik.webflux_addons.logging.filter.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class HeaderMessageFormatter implements RequestDataMessageFormatter {

    @Override
    public Mono<String> addData(ClientRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogHeaders()) {
            return sourceMessage.map(source -> source.concat(extractHeaders(request)));
        }

        return sourceMessage;
    }


    private String extractHeaders(ClientRequest request) {
        StringBuilder sb = new StringBuilder("\nHEADERS: [ ");

        request.headers()
                .forEach((headerName, headerValues) -> headerValues
                        .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));

        return sb.append("]").toString();
    }
}