package com.kiberohrannik.webflux_addons.logging.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseData;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;


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

    private MultiValueMap<String, String> setMask(ClientResponse response, String[] headerNames) {
        MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(response.headers().asHttpHeaders());

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