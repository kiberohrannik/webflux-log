package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.server.RequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public final class ReqIdMessageFormatter implements ServerMessageFormatter {

    @Override
    public Mono<RequestData> addData(LoggingProperties loggingProperties, Mono<RequestData> sourceMessage) {
        return null;
    }

//    @Override
//    public Mono<String> addData(ServerWebExchange exchange,
//                                LoggingProperties loggingProperties,
//                                Mono<String> sourceMessage) {
//
//        if (loggingProperties.isLogRequestId()) {
//            return sourceMessage.map(source -> source.concat(extractReqId(exchange, loggingProperties)));
//        }
//
//        return sourceMessage;
//    }
//
//    private String extractReqId(ServerWebExchange exchange, LoggingProperties loggingProperties) {
//        String reqId = LoggingUtils.extractReqId(exchange.getLogPrefix());
//
//        if (loggingProperties.getRequestIdPrefix() != null) {
//            reqId = loggingProperties.getRequestIdPrefix().concat("_").concat(reqId);
//        }
//
//        return " REQ-ID: [ ".concat(reqId).concat(" ]");
//    }
}