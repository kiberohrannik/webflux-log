package com.kiberohrannik.webflux_addons.logging.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class ReqIdMessageFormatter implements ResponseDataMessageFormatter {

    @Override
    public Mono<String> addData(ClientResponse response,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogRequestId()) {
            return sourceMessage.map(source -> source.concat(extractReqId(response, loggingProperties)));
        }

        return sourceMessage;
    }


    private String extractReqId(ClientResponse response, LoggingProperties loggingProperties) {
        String reqId = response.logPrefix();

        if (loggingProperties.getRequestIdPrefix() != null) {
            reqId = loggingProperties.getRequestIdPrefix().concat("_").concat(reqId);
        }

        return "\nREQ-ID: [ ".concat(reqId).concat(" ]");
    }
}