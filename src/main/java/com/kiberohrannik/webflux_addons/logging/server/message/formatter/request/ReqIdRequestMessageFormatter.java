package com.kiberohrannik.webflux_addons.logging.server.message.formatter.request;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;

@RequiredArgsConstructor
public final class ReqIdRequestMessageFormatter implements ServerMessageFormatter {

    @Override
    public String addData(ServerWebExchange exchange, LoggingProperties logProps, String source) {
        return logProps.isLogRequestId()
                ? source.concat(formatReqIdMessage(exchange.getLogPrefix(), logProps))
                : source;
    }


    private String formatReqIdMessage(String logPrefix, LoggingProperties loggingProperties) {
        String reqId = LoggingUtils.formatReqId(logPrefix);

        if (loggingProperties.getRequestIdPrefix() != null) {
            reqId = loggingProperties.getRequestIdPrefix().concat("_").concat(reqId);
        }

        return " REQ-ID: [ ".concat(reqId).concat(" ]");
    }
}