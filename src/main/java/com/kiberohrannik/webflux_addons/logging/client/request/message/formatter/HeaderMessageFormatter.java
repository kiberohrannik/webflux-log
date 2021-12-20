package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public class HeaderMessageFormatter implements RequestDataMessageFormatter {

    @Override
    public Mono<String> addData(ClientRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogHeaders()) {
            return sourceMessage.map(source -> source.concat(extractHeaders(request, loggingProperties)));
        }

        return sourceMessage;
    }


    private String extractHeaders(ClientRequest request, LoggingProperties props) {
        StringBuilder sb = new StringBuilder("\nHEADERS: [ ");

        if (props.getMaskedHeaders() == null) {
            extractAll(request.headers(), sb);
        } else {
            extractAll(setMask(request, props.getMaskedHeaders()), sb);
        }

        return sb.append("]").toString();
    }

    private MultiValueMap<String, String> setMask(ClientRequest request, String[] headerNames) {
        MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(request.headers());

        for (String maskedHeaderName : headerNames) {
            if (headersToLog.getFirst(maskedHeaderName) != null) {
                headersToLog.put(maskedHeaderName, List.of(LoggingUtils.DEFAULT_MASK));
            }
        }
        return headersToLog;
    }

    private void extractAll(MultiValueMap<String, String> headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}