package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.util.MultiValueMap;

public final class HeaderProvider {

    public String createMessage(MultiValueMap<String, String> headersToLog, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" HEADERS: [ ");

        if (props.getMaskedHeaders() == null) {
            extractAll(headersToLog, sb);
        } else {
            extractAll(setMask(headersToLog, props.getMaskedHeaders()), sb);
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
