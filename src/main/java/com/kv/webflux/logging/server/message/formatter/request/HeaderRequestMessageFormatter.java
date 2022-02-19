package com.kv.webflux.logging.server.message.formatter.request;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.HeaderProvider;
import com.kv.webflux.logging.server.message.formatter.ServerMessageFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

public final class HeaderRequestMessageFormatter implements ServerMessageFormatter {

    private final HeaderProvider provider = new HeaderProvider();


    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        if (logProps.isLogHeaders()) {
            MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(exchange.getRequest().getHeaders());
            headersToLog.remove(HttpHeaders.COOKIE);
            headersToLog.remove(HttpHeaders.COOKIE.toLowerCase());

            return source.concat(provider.createMessage(headersToLog, logProps));
        }

        return source;
    }
}