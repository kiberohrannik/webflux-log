package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class HeaderProvider {

    //TODO refactor these methods !!!!

    public String createMessage(MultiValueMap<String, String> headers, LoggingProperties properties) {
        if (!properties.isLogHeaders()) {
            return LoggingUtils.EMPTY_MESSAGE;
        }

        StringBuilder sb = new StringBuilder(" HEADERS: [ ");
        MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(headers);

        headersToLog.remove(HttpHeaders.SET_COOKIE);
        headersToLog.remove(HttpHeaders.COOKIE);
        headersToLog.remove(HttpHeaders.COOKIE.toLowerCase());

        if (properties.getMaskedHeaders() == null) {
            extractAll(headersToLog, sb);
        } else {
            extractAll(setMask(headersToLog, properties.getMaskedHeaders()), sb);
        }

        return sb.append("]").toString();
    }

    public String createMessage(MultiValueMap<String, String> headersToLog, String[] maskedHeaders) {
        StringBuilder sb = new StringBuilder(" HEADERS: [ ");

        if (maskedHeaders == null) {
            extractAll(headersToLog, sb);
        } else {
            extractAll(setMask(headersToLog, maskedHeaders), sb);
        }

        return sb.append("]").toString();
    }


    private MultiValueMap<String, String> setMask(MultiValueMap<String, String> headers, String[] headerNames) {
        return ProviderUtils.setMaskToValues(headers, headerNames, LoggingUtils.DEFAULT_MASK);
    }

    private void extractAll(MultiValueMap<String, String> headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}
