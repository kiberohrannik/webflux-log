package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;

public final class HeaderProvider {

    public String createMessage(MultiValueMap<String, String> headersToLog, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" HEADERS: [ ");

        if (props.getMaskedHeaders() != null) {
            setMask(headersToLog, props.getMaskedHeaders());
        }
        extractAll(headersToLog, sb);

        return sb.append("]").toString();
    }


    private void setMask(MultiValueMap<String, String> headers, String[] headerNames) {
        for (String maskedHeaderName : headerNames) {
            if (headers.getFirst(maskedHeaderName) != null) {
                headers.put(maskedHeaderName, List.of(LoggingUtils.DEFAULT_MASK));
            }
        }
    }

    private void extractAll(MultiValueMap<String, String> headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}
