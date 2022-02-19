package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public final class HeaderProvider {

    public String createMessage(MultiValueMap<String, String> headers, LoggingProperties properties) {
        if (!properties.isLogHeaders()) {
            return LoggingUtils.EMPTY_MESSAGE;
        }

        LinkedCaseInsensitiveMap<List<String>> headersToLog = new LinkedCaseInsensitiveMap<>();
        headersToLog.putAll(headers);

        removeCookieHeaders(headersToLog);

        StringBuilder sb = new StringBuilder(" HEADERS: [ ");

        if (properties.getMaskedHeaders() == null) {
            extractAll(headersToLog, sb);
        } else {
            extractAll(setMask(headersToLog, properties.getMaskedHeaders()), sb);
        }

        return sb.append("]").toString();
    }


    private void removeCookieHeaders(LinkedCaseInsensitiveMap<List<String>> headers) {
        headers.remove(HttpHeaders.SET_COOKIE);
        headers.remove(HttpHeaders.COOKIE);
    }

    private void extractAll(Map<String, List<String>> headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }

    private Map<String, List<String>> setMask(LinkedCaseInsensitiveMap<List<String>> headers, String[] headerNames) {
        return ProviderUtils.setMaskToValues(headers, headerNames, LoggingUtils.DEFAULT_MASK);
    }
}