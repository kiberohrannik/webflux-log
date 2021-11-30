package com.kiberohrannik.webflux_addons.logging.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.LoggingUtils;
import org.springframework.http.HttpHeaders;
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

    private HttpHeaders setMask(ClientRequest request, String[] headerNames) {
        HttpHeaders headersToLog = HttpHeaders.writableHttpHeaders(request.headers());

        for (String maskedHeaderName : headerNames) {
            if (headersToLog.getFirst(maskedHeaderName) != null) {
                headersToLog.put(maskedHeaderName, List.of(LoggingUtils.DEFAULT_MASK));
            }
        }

        return headersToLog;
    }

    private void extractAll(HttpHeaders headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}