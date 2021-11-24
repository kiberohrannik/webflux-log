package com.kiberohrannik.webflux_addons.logging.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseData;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.kiberohrannik.webflux_addons.logging.LoggingProperties.DEFAULT_MASK;

public class HeaderMessageFormatter implements ResponseDataMessageFormatter {

    @Override
    public Mono<ResponseData> addData(LoggingProperties loggingProperties,
                                      Mono<ResponseData> sourceMessage) {

        if (loggingProperties.isLogHeaders()) {
            return sourceMessage.map(source -> {
                String headersMessage = extractHeaders(source.getResponse(), loggingProperties);
                return source.addToLogs(headersMessage);
            });
        }

        return sourceMessage;
    }


    private String extractHeaders(ClientResponse response, LoggingProperties props) {
        StringBuilder sb = new StringBuilder("\nHEADERS: [ ");

        if (props.getMaskedHeaders() == null) {
            extractAll(response.headers().asHttpHeaders(), sb);
        } else {
            extractAll(setMask(response, props.getMaskedHeaders()), sb);
        }

        return sb.append("]").toString();
    }

    private HttpHeaders setMask(ClientResponse response, String[] headerNames) {
        HttpHeaders headersToLog = HttpHeaders.writableHttpHeaders(response.headers().asHttpHeaders());

        for (String sensitiveHeaderName : headerNames) {
            headersToLog.put(sensitiveHeaderName, List.of(DEFAULT_MASK));
        }

        return headersToLog;
    }

    private void extractAll(HttpHeaders headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}