package com.kiberohrannik.webflux_addons.logging.extractor;

import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;

public final class HeaderExtractor {

    public void setMask(MultiValueMap<String, String> headers, String[] headerNames) {
        for (String maskedHeaderName : headerNames) {
            if (headers.getFirst(maskedHeaderName) != null) {
                headers.put(maskedHeaderName, List.of(LoggingUtils.DEFAULT_MASK));
            }
        }
    }

    public void extractAll(MultiValueMap<String, String> headers, StringBuilder sb) {
        headers.forEach((headerName, headerValues) -> headerValues
                .forEach(value -> sb.append(headerName).append("=").append(value).append(" ")));
    }
}
