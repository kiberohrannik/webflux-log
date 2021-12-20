package com.kiberohrannik.webflux_addons.logging.extractor;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public final class HeaderExtractor {

    public String extractHeaders(HttpHeaders httpHeaders, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" HEADERS: [ ");

        if (props.getMaskedHeaders() == null) {
            extractAll(httpHeaders, sb);
        } else {
            extractAll(setMask(httpHeaders, props.getMaskedHeaders()), sb);
        }

        return sb.append("]").toString();
    }


    private MultiValueMap<String, String> setMask(HttpHeaders httpHeaders, String[] headerNames) {
        MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(httpHeaders);

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
