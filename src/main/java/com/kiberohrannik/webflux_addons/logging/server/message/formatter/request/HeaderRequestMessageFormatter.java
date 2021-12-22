package com.kiberohrannik.webflux_addons.logging.server.message.formatter.request;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

@RequiredArgsConstructor
public final class HeaderRequestMessageFormatter implements ServerMessageFormatter {

    private final HeaderProvider provider;


    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        if (logProps.isLogHeaders()) {
            MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(exchange.getRequest().getHeaders());
            headersToLog.remove(HttpHeaders.COOKIE);

            return source.concat(provider.createMessage(headersToLog, logProps));
        }

        return source;
    }
}