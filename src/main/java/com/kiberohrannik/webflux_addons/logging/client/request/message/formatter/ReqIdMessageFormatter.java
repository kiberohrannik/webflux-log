package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class ReqIdMessageFormatter implements RequestDataMessageFormatter {

    @Override
    public Mono<String> addData(ClientRequest request, LoggingProperties logProps, Mono<String> source) {
        if (logProps.isLogRequestId()) {
            return source.map(message -> message.concat(extractReqId(request, logProps)));
        }

        return source;
    }


    private String extractReqId(ClientRequest request, LoggingProperties logProps) {
        String reqId = LoggingUtils.formatReqId(request.logPrefix());

        if (logProps.getRequestIdPrefix() != null) {
            reqId = logProps.getRequestIdPrefix().concat("_").concat(reqId);
        }

        return "\nREQ-ID: [ ".concat(reqId).concat(" ]");
    }
}