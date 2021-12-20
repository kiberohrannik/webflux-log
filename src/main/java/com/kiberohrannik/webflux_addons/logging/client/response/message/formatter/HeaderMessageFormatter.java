package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import org.springframework.http.HttpHeaders;
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
                String headersMessage = formatHeaderMessage(source.getResponse(), loggingProperties);
                return source.addToLogs(headersMessage);
            });
        }

        return sourceMessage;
    }


    private String formatHeaderMessage(ClientResponse response, LoggingProperties props) {
        StringBuilder sb = new StringBuilder("\nHEADERS: [ ");

        MultiValueMap<String, String> headersToLog = ignoreCookies(response.headers());

        if (props.getMaskedHeaders() == null) {
            extractAll(headersToLog, sb);
        } else {
            extractAll(setMask(headersToLog, props), sb);
        }

        return sb.append("]").toString();
    }

    private MultiValueMap<String, String> ignoreCookies(ClientResponse.Headers sourceHeaders) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(sourceHeaders.asHttpHeaders());
        headers.remove(HttpHeaders.SET_COOKIE);

        return headers;
    }

    private MultiValueMap<String, String> setMask(MultiValueMap<String, String> headers, LoggingProperties props) {
        for (String maskedHeaderName : props.getMaskedHeaders()) {
            if (headers.getFirst(maskedHeaderName) != null) {
                headers.put(maskedHeaderName, List.of(LoggingUtils.DEFAULT_MASK));
            }
        }

        return headers;
    }

    private void extractAll(MultiValueMap<String, String> headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}