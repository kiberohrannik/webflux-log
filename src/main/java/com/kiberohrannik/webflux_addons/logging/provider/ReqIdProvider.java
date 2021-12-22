package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;

public final class ReqIdProvider {

    public String createMessage(String logPrefix, LoggingProperties logProps) {
        String reqId = formatReqId(logPrefix);

        if (logProps.getRequestIdPrefix() != null) {
            reqId = logProps.getRequestIdPrefix().concat("_").concat(reqId);
        }

        return " REQ-ID: [ ".concat(reqId).concat(" ]");
    }


    private String formatReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }
}
