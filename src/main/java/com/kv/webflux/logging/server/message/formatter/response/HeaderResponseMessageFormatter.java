package com.kv.webflux.logging.server.message.formatter.response;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.HeaderProvider;
import com.kv.webflux.logging.server.message.formatter.ServerMessageFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

public final class HeaderResponseMessageFormatter implements ServerMessageFormatter {

    private final HeaderProvider provider = new HeaderProvider();


    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        if (logProps.isLogHeaders()) {
            MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(exchange.getResponse().getHeaders());
            headersToLog.remove(HttpHeaders.SET_COOKIE);

            return source.concat(provider.createMessage(headersToLog, logProps));
        }

        return source;
    }
}