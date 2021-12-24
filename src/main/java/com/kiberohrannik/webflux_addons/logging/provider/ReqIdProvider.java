package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;

public final class ReqIdProvider {

    public String createFromLogPrefix(String logPrefix, LoggingProperties logProps, String sourceMessage) {
        return logProps.isLogRequestId()
                ? sourceMessage.concat(create(formatReqId(logPrefix), logProps.getRequestIdPrefix()))
                : sourceMessage;
    }

    public String createFromLogId(String logId, LoggingProperties logProps) {
        return logProps.isLogRequestId()
                ? create(logId, logProps.getRequestIdPrefix()).trim()
                : "";
    }


    private String create(String reqId, String reqIdPrefix) {
        if (reqIdPrefix != null) {
            reqId = reqIdPrefix.concat("_").concat(reqId);
        }

        return " REQ-ID: [ ".concat(reqId).concat(" ]");
    }

    private String formatReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }
}
