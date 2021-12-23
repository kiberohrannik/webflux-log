package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

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
            List<String> values = headers.get(maskedHeaderName);

            if (values != null && !values.isEmpty()) {
                if (values.size() == 1) {
                    headers.put(maskedHeaderName, List.of(LoggingUtils.DEFAULT_MASK));
                    return;
                }

                List<String> masked = values.stream()
                        .map(value -> LoggingUtils.DEFAULT_MASK)
                        .collect(Collectors.toList());

                headers.put(maskedHeaderName, masked);
            }
        }
    }

    private void extractAll(MultiValueMap<String, String> headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}
