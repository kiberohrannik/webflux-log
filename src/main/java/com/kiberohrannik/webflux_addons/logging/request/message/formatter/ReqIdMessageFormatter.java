package com.kiberohrannik.webflux_addons.logging.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

public class ReqIdMessageFormatter implements RequestDataMessageFormatter {

    @Override
    public Mono<String> addData(ClientRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogRequestId()) {
            return sourceMessage.map(source -> source.concat(extractReqId(request, loggingProperties)));
        }

        return sourceMessage;
    }


    private String extractReqId(ClientRequest request, LoggingProperties loggingProperties) {
        String reqId = request.logPrefix().substring(1, request.logPrefix().length() - 2);

        if (loggingProperties.getRequestIdPrefix() != null) {
            reqId = loggingProperties.getRequestIdPrefix().concat("_").concat(reqId);
        }

        return "\nREQ-ID: [ ".concat(reqId).concat(" ]");
    }
}